package no.stockwallet;

import com.crazzyghost.alphavantage.AlphaVantage;

import no.stockwallet.Investment;
import no.stockwallet.ValueSetterSupport;

public class StockCalculations {






    public double currencyConverter(Investment foreignCurrencyInvestment) {
        ValueSetterSupport valueSetterSupport = new ValueSetterSupport();

        Thread thread = new Thread(() -> {

            AlphaVantage.api()
                    .exchangeRate()
                    .fromCurrency(foreignCurrencyInvestment.currency)
                    .toCurrency("NOK")
                    .onSuccess(e -> {
                        valueSetterSupport.setReturnValue((foreignCurrencyInvestment.getPrice() * e.getExchangeRate()));
                    })
                    .onFailure(Throwable::printStackTrace)
                    .fetch();
        });

        thread.start();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return valueSetterSupport.getReturnValue();

    }

    public double getTotalInvestments(){
        double totSumInvested = 0;
        for (Investment x: investments.values()){
            if (!x.currency.equals("NOK")){
                totSumInvested += (currencyConverter(x) * x.volum);
            }
            else {
                totSumInvested += x.price * x.volum;            //TODO: må endres tilå hente nå pris fremfor kjøpt-pris
            }
        }
        return totSumInvested;
    }



    public void checkIfAlreadyInvestedIn(String key){

        if (investments.containsKey(key)){
            investments.get(key);
        }
    }



















}
