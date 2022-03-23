package com.example.launchdinner;

//data class
class Apply {
    private String seq;
    private String startAddress;
    private String endAddress;
    private String departure_time;
    private String arrive_time;
    private String exchange_item;
    private String delivery_yn;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getExchange_item() {
        return exchange_item;
    }

    public void setExchange_item(String exchange_item) {
        this.exchange_item = exchange_item;
    }

    public String getDelivery_yn() {
        return delivery_yn;
    }

    public void setDelivery_yn(String delivery_yn) {
        this.delivery_yn = delivery_yn;
    }
}
