package com.proyect.mvp.domain.model.patterns.strategy_MakePrice;

import com.proyect.mvp.domain.model.entities.ProductEntity;

public class MakePriceTwo implements MakePrice{
    public void makePrice(ProductEntity product){
        Double price = product.getUnity_price();
        product.setPrice(price + price * 0.1);
    }
}
