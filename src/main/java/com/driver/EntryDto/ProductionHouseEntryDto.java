package com.driver.EntryDto;

public class ProductionHouseEntryDto {

    private String name;

    // no args constructor
    public ProductionHouseEntryDto(){
      //  use for jackson
    }

    public ProductionHouseEntryDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
