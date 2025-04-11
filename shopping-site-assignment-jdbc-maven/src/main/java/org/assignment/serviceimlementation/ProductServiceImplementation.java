package org.assignment.serviceimlementation;

import lombok.RequiredArgsConstructor;
import org.assignment.entities.User;
import org.assignment.entities.Product;
import org.assignment.enums.Currency;
import org.assignment.enums.ProductType;
import org.assignment.enums.ResponseStatus;
import org.assignment.repository.interfaces.UserRepository;
import org.assignment.repository.interfaces.ProductRepository;
import org.assignment.services.ProductService;
import org.assignment.util.Constants;
import org.assignment.util.Response;
import org.assignment.wrappers.ProductWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ProductRepository productRepository;
private final UserRepository userRepository;
    @Override
    public Response getAllProduct() {
        Response response;
        try {
            List<Product> products = productRepository.fetchProducts();

            response = new Response(ResponseStatus.SUCCESSFUL, getListOfProductWrappers(products), null);
        } catch (SQLException e) {
            log.error("Some error occured while fetching products from repository", e);
            response = new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        return response;
    }

    @Override
    public Response hasProduct(User user, String productName) {
        boolean exists = user
                .getCart()
                .stream()
                .anyMatch(item -> item
                        .getProduct()
                        .getName()
                        .equalsIgnoreCase(productName));
        return new Response(ResponseStatus.SUCCESSFUL, exists, null);
    }

    @Override
    public Response getProductsByType(ProductType type) {
        List<Product> products;// fetches List of product to be searched in.
        try {
            products = productRepository.fetchProducts();
        } catch (SQLException e) {
            log.error("Error occured while retrieving product basen on types : ", e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if (products.isEmpty()) {
            return new Response(ResponseStatus.ERROR, null, "Could not find any product based on provided type");
        }
        Map<ProductType, List<Product>> map = products
                .stream()
                .collect(Collectors.groupingBy(Product::getType));

        if (!map.containsKey(type)) {
            return new Response(ResponseStatus.ERROR, null, "No product found for this category"); // throws exception if no product is found for the provided type.
        }
        List<Product> products1 = map.get(type);
        return new Response(ResponseStatus.SUCCESSFUL,getListOfProductWrappers(products1), null);
    }

    @Override
    public Response addProduct(String name, long seller, int typeIndex, int currencyType, double price, int stock) {
        ProductType[] types = ProductType.values();
        Currency[] currency = Currency.values();
        boolean isLesserThanZero = Boolean.TRUE.equals(typeIndex < 0);
        boolean isGreaterThanOREqualsToArraySize = Boolean.TRUE.equals(typeIndex >= types.length);
        if(Boolean.logicalOr(isLesserThanZero, isGreaterThanOREqualsToArraySize)){
            return new Response(ResponseStatus.ERROR,null,  "Cannot find requested Product type");
        }
        isLesserThanZero = Boolean.TRUE.equals(currencyType < 0);
        isGreaterThanOREqualsToArraySize = Boolean.TRUE.equals(currencyType >= currency.length);
        if(Boolean.logicalOr(isLesserThanZero, isGreaterThanOREqualsToArraySize)){
            return new Response(ResponseStatus.ERROR,null,  "Cannot find the currency");
        }
        Optional<User> sellerOptional;
        try {
             sellerOptional = userRepository.fetchSellerById(seller);
        }catch (Exception e)
        {
            log.error("Some error occured while fetching seller based on id {} for product addition ", seller, e);
            return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
        }
        if(sellerOptional.isEmpty())
        {
            return new Response(ResponseStatus.ERROR,null,  "Invalid Seller id");
        }
try {
    if (productRepository.fetchProductByName(name).isPresent()) {
        return new Response(ResponseStatus.ERROR,null,  "Product with same name already exist");
    }
}catch (Exception e){
    log.error("Some error occured while fetching product based on name {} for product addition ", name);
    return new Response(ResponseStatus.ERROR, null, Constants.ERROR_MESSAGE);
}
ProductType type = types[typeIndex];
Currency productCurrency = currency[currencyType];
User sellerObject = sellerOptional.get();
Product newProduct = Product.builder()
        .currency(productCurrency)
        .seller(sellerObject)
        .name(name)
        .type(type)
        .price(price)
        .stock(stock)
        .build();
productRepository.addProduct(newProduct);
return new Response(ResponseStatus.SUCCESSFUL, "Product saved!! ", null);
    }
    private List<ProductWrapper> getListOfProductWrappers(List<Product> products)
    {
        List<ProductWrapper> wrapperList = new ArrayList<>();
        products.stream()
                .filter(product -> product.getStock() > 0)
                .forEach(p ->
                        wrapperList.add(new ProductWrapper(p))
                );
        return wrapperList;
    }
}
