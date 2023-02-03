package host.luke.common.pojo;

import lombok.Data;

@Data

public class Consumption {

    private Long consumptionId;
    private Double amount;
    private String type;//dinning travel entertainment study daily others
    private String store;
    private Integer year;
    private Integer month;
    private Integer day;


}
