package cookbook.model;

public class Ingredient {
    private String name;
    private int amount;
    private String unit;

    public void setName(String name){
        this.name = name;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public void setUnit(String unit){
        this.unit = unit;
    }

    public String getName(){
        return name;
    }
    public int getAmount(){
        return amount;
    }
    public String getUnit(){
        return unit;
    }


}