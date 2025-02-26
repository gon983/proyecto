package com.proyect.mvp.domain.model.patterns.strategy_MakePrice;

public class MakePriceFactory {
    /**
     * Retorna la implementación de MakePrice correspondiente al nivel del usuario
     * 
     * @param level El nivel del usuario
     * @return La estrategia de cálculo de precio correspondiente
     */
    public static MakePrice getStrategy(int level) {
        switch (level) {
            case 1:
                return new MakePriceOne();
            case 2:
                return new MakePriceTwo();
            case 3:
                return new MakePriceThree();
            case 4:
                return new MakePriceFour();
            case 5:
                return new MakePriceFive();
            case 6:
                return new MakePriceSix();
            case 7:
                return new MakePriceSeven();
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
    }
    
}
