package weather.restdto;

public class SearchSearchApiDto {
    private final SearchSearchApiResultDto[] result;

    public SearchSearchApiDto(SearchSearchApiResultDto[] result) {
        this.result = result;
    }

    public SearchSearchApiResultDto[] getResult() {
        return result;
    }
}
