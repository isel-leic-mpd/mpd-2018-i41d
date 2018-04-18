package weather.restdto;

public class SearchDto {
    private final SearchSearchApiDto search_api;

    public SearchDto(SearchSearchApiDto search_api) {
        this.search_api = search_api;
    }

    public SearchSearchApiDto getSearch_api() {
        return search_api;
    }
}
