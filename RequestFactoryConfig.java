@Component
@Configuration
public class RequestFactoryConfig {

    @Autowired
    private OkHttpClientFactoryImpl httpClientFactory;

    @Bean
    @Qualifier("createOKCommonsRequestFactory")
    public ClientHttpRequestFactory createOKCommonsRequestFactory() {
        OkHttpClient client = httpClientFactory.createBuilder(false).build();
        return new OkHttp3ClientHttpRequestFactory(client);
    }
}
