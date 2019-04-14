package in.ravi275.ArduinoControl;


public class DeviceCard {
	
	
    private String name;
    private String address;

    public DeviceCard(String name, String address) {
		
        this.name = name;
        this.address = address;
    }
	

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}
