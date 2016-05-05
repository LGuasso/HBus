/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-04-08 17:16:44 UTC)
 * on 2016-05-04 at 00:12:53 UTC 
 * Modify at your own risk.
 */

package br.com.expressobits.hbus.backend.busApi;

/**
 * Service definition for BusApi (v1).
 *
 * <p>
 * This is an API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link BusApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class BusApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the busApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://hbus-1206.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "busApi/v1/bus/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public BusApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  BusApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "clearBus".
   *
   * This request holds the parameters needed by the busApi server.  After setting any optional
   * parameters, call the {@link ClearBus#execute()} method to invoke the remote operation.
   *
   * @param country
   * @param cityName
   * @return the request
   */
  public ClearBus clearBus(java.lang.String country, java.lang.String cityName) throws java.io.IOException {
    ClearBus result = new ClearBus(country, cityName);
    initialize(result);
    return result;
  }

  public class ClearBus extends BusApiRequest<Void> {

    private static final String REST_PATH = "{country}/{cityName}";

    /**
     * Create a request for the method "clearBus".
     *
     * This request holds the parameters needed by the the busApi server.  After setting any optional
     * parameters, call the {@link ClearBus#execute()} method to invoke the remote operation. <p>
     * {@link
     * ClearBus#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param country
     * @param cityName
     * @since 1.13
     */
    protected ClearBus(java.lang.String country, java.lang.String cityName) {
      super(BusApi.this, "POST", REST_PATH, null, Void.class);
      this.country = com.google.api.client.util.Preconditions.checkNotNull(country, "Required parameter country must be specified.");
      this.cityName = com.google.api.client.util.Preconditions.checkNotNull(cityName, "Required parameter cityName must be specified.");
    }

    @Override
    public ClearBus setAlt(java.lang.String alt) {
      return (ClearBus) super.setAlt(alt);
    }

    @Override
    public ClearBus setFields(java.lang.String fields) {
      return (ClearBus) super.setFields(fields);
    }

    @Override
    public ClearBus setKey(java.lang.String key) {
      return (ClearBus) super.setKey(key);
    }

    @Override
    public ClearBus setOauthToken(java.lang.String oauthToken) {
      return (ClearBus) super.setOauthToken(oauthToken);
    }

    @Override
    public ClearBus setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ClearBus) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ClearBus setQuotaUser(java.lang.String quotaUser) {
      return (ClearBus) super.setQuotaUser(quotaUser);
    }

    @Override
    public ClearBus setUserIp(java.lang.String userIp) {
      return (ClearBus) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String country;

    /**

     */
    public java.lang.String getCountry() {
      return country;
    }

    public ClearBus setCountry(java.lang.String country) {
      this.country = country;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String cityName;

    /**

     */
    public java.lang.String getCityName() {
      return cityName;
    }

    public ClearBus setCityName(java.lang.String cityName) {
      this.cityName = cityName;
      return this;
    }

    @Override
    public ClearBus set(String parameterName, Object value) {
      return (ClearBus) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getBus".
   *
   * This request holds the parameters needed by the busApi server.  After setting any optional
   * parameters, call the {@link GetBus#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetBus getBus(java.lang.Long id) throws java.io.IOException {
    GetBus result = new GetBus(id);
    initialize(result);
    return result;
  }

  public class GetBus extends BusApiRequest<br.com.expressobits.hbus.backend.busApi.model.Bus> {

    private static final String REST_PATH = "{id}";

    /**
     * Create a request for the method "getBus".
     *
     * This request holds the parameters needed by the the busApi server.  After setting any optional
     * parameters, call the {@link GetBus#execute()} method to invoke the remote operation. <p> {@link
     * GetBus#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetBus(java.lang.Long id) {
      super(BusApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.busApi.model.Bus.class);
      this.id = com.google.api.client.util.Preconditions.checkNotNull(id, "Required parameter id must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetBus setAlt(java.lang.String alt) {
      return (GetBus) super.setAlt(alt);
    }

    @Override
    public GetBus setFields(java.lang.String fields) {
      return (GetBus) super.setFields(fields);
    }

    @Override
    public GetBus setKey(java.lang.String key) {
      return (GetBus) super.setKey(key);
    }

    @Override
    public GetBus setOauthToken(java.lang.String oauthToken) {
      return (GetBus) super.setOauthToken(oauthToken);
    }

    @Override
    public GetBus setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetBus) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetBus setQuotaUser(java.lang.String quotaUser) {
      return (GetBus) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetBus setUserIp(java.lang.String userIp) {
      return (GetBus) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public GetBus setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public GetBus set(String parameterName, Object value) {
      return (GetBus) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getBuses".
   *
   * This request holds the parameters needed by the busApi server.  After setting any optional
   * parameters, call the {@link GetBuses#execute()} method to invoke the remote operation.
   *
   * @param country
   * @param cityName
   * @param itineraryName
   * @return the request
   */
  public GetBuses getBuses(java.lang.String country, java.lang.String cityName, java.lang.String itineraryName) throws java.io.IOException {
    GetBuses result = new GetBuses(country, cityName, itineraryName);
    initialize(result);
    return result;
  }

  public class GetBuses extends BusApiRequest<br.com.expressobits.hbus.backend.busApi.model.BusCollection> {

    private static final String REST_PATH = "{country}/{cityName}/{itineraryName}";

    /**
     * Create a request for the method "getBuses".
     *
     * This request holds the parameters needed by the the busApi server.  After setting any optional
     * parameters, call the {@link GetBuses#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetBuses#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param country
     * @param cityName
     * @param itineraryName
     * @since 1.13
     */
    protected GetBuses(java.lang.String country, java.lang.String cityName, java.lang.String itineraryName) {
      super(BusApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.busApi.model.BusCollection.class);
      this.country = com.google.api.client.util.Preconditions.checkNotNull(country, "Required parameter country must be specified.");
      this.cityName = com.google.api.client.util.Preconditions.checkNotNull(cityName, "Required parameter cityName must be specified.");
      this.itineraryName = com.google.api.client.util.Preconditions.checkNotNull(itineraryName, "Required parameter itineraryName must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetBuses setAlt(java.lang.String alt) {
      return (GetBuses) super.setAlt(alt);
    }

    @Override
    public GetBuses setFields(java.lang.String fields) {
      return (GetBuses) super.setFields(fields);
    }

    @Override
    public GetBuses setKey(java.lang.String key) {
      return (GetBuses) super.setKey(key);
    }

    @Override
    public GetBuses setOauthToken(java.lang.String oauthToken) {
      return (GetBuses) super.setOauthToken(oauthToken);
    }

    @Override
    public GetBuses setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetBuses) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetBuses setQuotaUser(java.lang.String quotaUser) {
      return (GetBuses) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetBuses setUserIp(java.lang.String userIp) {
      return (GetBuses) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String country;

    /**

     */
    public java.lang.String getCountry() {
      return country;
    }

    public GetBuses setCountry(java.lang.String country) {
      this.country = country;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String cityName;

    /**

     */
    public java.lang.String getCityName() {
      return cityName;
    }

    public GetBuses setCityName(java.lang.String cityName) {
      this.cityName = cityName;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String itineraryName;

    /**

     */
    public java.lang.String getItineraryName() {
      return itineraryName;
    }

    public GetBuses setItineraryName(java.lang.String itineraryName) {
      this.itineraryName = itineraryName;
      return this;
    }

    @Override
    public GetBuses set(String parameterName, Object value) {
      return (GetBuses) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertBus".
   *
   * This request holds the parameters needed by the busApi server.  After setting any optional
   * parameters, call the {@link InsertBus#execute()} method to invoke the remote operation.
   *
   * @param country
   * @param cityName
   * @param itineraryName
   * @param content the {@link br.com.expressobits.hbus.backend.busApi.model.Bus}
   * @return the request
   */
  public InsertBus insertBus(java.lang.String country, java.lang.String cityName, java.lang.String itineraryName, br.com.expressobits.hbus.backend.busApi.model.Bus content) throws java.io.IOException {
    InsertBus result = new InsertBus(country, cityName, itineraryName, content);
    initialize(result);
    return result;
  }

  public class InsertBus extends BusApiRequest<br.com.expressobits.hbus.backend.busApi.model.Bus> {

    private static final String REST_PATH = "{country}/{cityName}/{itineraryName}";

    /**
     * Create a request for the method "insertBus".
     *
     * This request holds the parameters needed by the the busApi server.  After setting any optional
     * parameters, call the {@link InsertBus#execute()} method to invoke the remote operation. <p>
     * {@link
     * InsertBus#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param country
     * @param cityName
     * @param itineraryName
     * @param content the {@link br.com.expressobits.hbus.backend.busApi.model.Bus}
     * @since 1.13
     */
    protected InsertBus(java.lang.String country, java.lang.String cityName, java.lang.String itineraryName, br.com.expressobits.hbus.backend.busApi.model.Bus content) {
      super(BusApi.this, "POST", REST_PATH, content, br.com.expressobits.hbus.backend.busApi.model.Bus.class);
      this.country = com.google.api.client.util.Preconditions.checkNotNull(country, "Required parameter country must be specified.");
      this.cityName = com.google.api.client.util.Preconditions.checkNotNull(cityName, "Required parameter cityName must be specified.");
      this.itineraryName = com.google.api.client.util.Preconditions.checkNotNull(itineraryName, "Required parameter itineraryName must be specified.");
    }

    @Override
    public InsertBus setAlt(java.lang.String alt) {
      return (InsertBus) super.setAlt(alt);
    }

    @Override
    public InsertBus setFields(java.lang.String fields) {
      return (InsertBus) super.setFields(fields);
    }

    @Override
    public InsertBus setKey(java.lang.String key) {
      return (InsertBus) super.setKey(key);
    }

    @Override
    public InsertBus setOauthToken(java.lang.String oauthToken) {
      return (InsertBus) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertBus setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertBus) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertBus setQuotaUser(java.lang.String quotaUser) {
      return (InsertBus) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertBus setUserIp(java.lang.String userIp) {
      return (InsertBus) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String country;

    /**

     */
    public java.lang.String getCountry() {
      return country;
    }

    public InsertBus setCountry(java.lang.String country) {
      this.country = country;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String cityName;

    /**

     */
    public java.lang.String getCityName() {
      return cityName;
    }

    public InsertBus setCityName(java.lang.String cityName) {
      this.cityName = cityName;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String itineraryName;

    /**

     */
    public java.lang.String getItineraryName() {
      return itineraryName;
    }

    public InsertBus setItineraryName(java.lang.String itineraryName) {
      this.itineraryName = itineraryName;
      return this;
    }

    @Override
    public InsertBus set(String parameterName, Object value) {
      return (InsertBus) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link BusApi}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link BusApi}. */
    @Override
    public BusApi build() {
      return new BusApi(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link BusApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setBusApiRequestInitializer(
        BusApiRequestInitializer busapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(busapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
