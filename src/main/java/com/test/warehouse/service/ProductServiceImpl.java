package com.test.warehouse.service;

import com.test.warehouse.dao.ProductDao;
import com.test.warehouse.dao.dto.AvailableProductDto;
import com.test.warehouse.dao.exception.InsufficientAmountInStock;
import com.test.warehouse.model.Product;
import com.test.warehouse.service.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.test.warehouse.service.util.ErrorLogger.logError;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;

    @Override
    public void saveAll(List<Product> products) throws ServiceException {
        try {
            productDao.saveAll(products);
            log.info("Successfully saved all products");
        } catch (Exception e) {
            String errMsg = "Error saving all products";
            logError(ProductServiceImpl.class, e, errMsg);
            throw new ServiceException(errMsg, e);
        }
    }

    @Override
    public List<AvailableProductDto> listAvailable() throws ServiceException {
        try {
            return productDao.listAvailable();
        } catch (Exception e) {
            String errMsg = "Error getting all available products";
            logError(ProductServiceImpl.class, e, errMsg);
            throw new ServiceException(errMsg, e);
        }
    }

    @Override
    public void sellProduct(String name, int amount) throws ServiceException, InsufficientAmountInStock {
        try {
            productDao.sellProduct(name, amount);
            log.info("Successfully sold product name '{}', amount {}", name, amount);
        } catch (InsufficientAmountInStock e) {
            Optional<AvailableProductDto> availableProduct = productDao.getIfAvailable(name);
            int availableAmount = availableProduct.map(AvailableProductDto::getAvailableAmount).orElse(0);
            String errMsg = String.format("Failed to sell product '%s': no such quantity in stock." +
                    " Requested:%d, available:%d", name, amount, availableAmount);
            log.warn(errMsg);
            throw new InsufficientAmountInStock(errMsg);
        } catch (IncorrectResultSizeDataAccessException e) {
            log.warn("Nothing was updated after attempt to sell product '{}', amount {}. Check if parameters are valid", name, amount);
            throw new ServiceException(e.getMessage());
        } catch (Exception e) {
            String errMsg = String.format("Error selling product: name: %s, amount: %s", name, amount);
            logError(ProductServiceImpl.class, e, errMsg);
            throw new ServiceException(errMsg, e);
        }
    }
}
