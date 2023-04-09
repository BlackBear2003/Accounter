package host.luke.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class Sentence {
    String body;
    String slicedBody;
    List<String> words;
}
