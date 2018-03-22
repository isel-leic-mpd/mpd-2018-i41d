package weather.restdto;

public class ContainerDto {
    private final String value;

    public ContainerDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
