# Mise en place de swagger dans un projet :
 
## Le pom.xml
Modifier le pom.xml afin d'ajout la dépendance  suivante :
```xml
       <properties>
             <swagger-version>1.5.8</swagger-version>
       </properties>
...
		<dependency>
			<groupId>io.swagger</groupId>
			<artifactId>swagger-jaxrs</artifactId>
			<version>${swagger-version}</version>
			<scope>compile</scope>
			<exclusions>
				<exclusion>
					<groupId>javax.ws.rs</groupId>
					<artifactId>jsr311-api</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
```
il faut exclure la librairie JSR-311, elle cause des problèmes de dépendance.
 
## ajout du site swagger
Puis, mettre tous les codes suivant de "https://github.com/swagger-api/swagger-ui/tree/master/dist" dans "src\main\webapp".
Editer la ligne 39 de index.html pour mettre la page désirée :
url = "http://localhost:8080/myws/api/swagger.json";

 
## Intégration des api-swagger de document par l'ajout dans RestApplicationConfiguration
Puis, dans la class de chargement REST, il faut ajouter :

```java
@ApplicationPath("api")
public class RestApplicationConfiguration extends Application {

  public RestApplicationConfiguration() {
    BeanConfig beanConfig = new BeanConfig();
    beanConfig.setVersion("1.0.2");
    beanConfig.setSchemes(new String[] { "http" });
    beanConfig.setHost("localhost:8080");
    beanConfig.setBasePath("/myws/api");
    beanConfig.setResourcePackage("org.jobjects.myws");
    beanConfig.setScan(true);
  }
  
  @Override
  public Set<Class<?>> getClasses() {
      Set<Class<?>> resources = new HashSet<Class<?>>();
      resources.add(CustomRequestWrapperFilter.class);
      resources.add(UserRESTWebService.class);
      resources.add(UserWriter.class);
      resources.add(UserReader.class);
      resources.add(TrafficLogger.class);
      resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
      resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
      return resources;
  }
}
```
 
## Exemple de documentation
Mettre l'annotation Api pour chaque class contenant des services web. Et utiliser pour chaque web server les annotations ApiOperation et ApiResponses pour chaque service web. D'autres annotations sont disponibles pour les champs et aussi JSON.

```java
@Api(value = "/user", description = "Operations about user")
@Path("/user")
@RequestScoped
@Tracked
public class UserRESTWebService {
  private transient Logger LOGGER = Logger.getLogger(getClass().getName());

  @Inject
  UserRepository userRepository;

  @ApiOperation(value = "Stocker un user.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "cas nominal."),
      @ApiResponse(code = 403, message = "Interdiction d'accès."),
      @ApiResponse(code = 500, message = "Erreur interne.") })
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(User user, @Context SecurityContext securityContext) {
```

That's all folks !

