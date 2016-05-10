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
 * (build: 2016-05-04 15:59:39 UTC)
 * on 2016-05-10 at 00:31:00 UTC 
 * Modify at your own risk.
 */

package br.com.expressobits.hbus.backend.cityApi;

/**
 * Service definition for CityApi (v1).
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
 * This service uses {@link CityApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class CityApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.22.0 of the cityApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
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
  public static final String DEFAULT_SERVICE_PATH = "cityApi/v1/";

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
  public CityApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  CityApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "clearCities".
   *
   * This request holds the parameters needed by the cityApi server.  After setting any optional
   * parameters, call the {@link ClearCities#execute()} method to invoke the remote operation.
   *
   * @param country
   * @return the request
   */
  public ClearCities clearCities(java.lang.String country) throws java.io.IOException {
    ClearCities result = new ClearCities(country);
    initialize(result);
    return result;
  }

  public class ClearCities extends CityApiRequest<Void> {

    private static final String REST_PATH = "city/{country}";

    /**
     * Create a request for the method "clearCities".
     *
     * This request holds the parameters needed by the the cityApi server.  After setting any optional
     * parameters, call the {@link ClearCities#execute()} method to invoke the remote operation. <p>
     * {@link
     * ClearCities#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param country
     * @since 1.13
     */
    protected ClearCities(java.lang.String country) {
      super(CityApi.this, "POST", REST_PATH, null, Void.class);
      this.country = com.google.api.client.util.Preconditions.checkNotNull(country, "Required parameter country must be specified.");
    }

    @Override
    public ClearCities setAlt(java.lang.String alt) {
      return (ClearCities) super.setAlt(alt);
    }

    @Override
    public ClearCities setFields(java.lang.String fields) {
      return (ClearCities) super.setFields(fields);
    }

    @Override
    public ClearCities setKey(java.lang.String key) {
      return (ClearCities) super.setKey(key);
    }

    @Override
    public ClearCities setOauthToken(java.lang.String oauthToken) {
      return (ClearCities) super.setOauthToken(oauthToken);
    }

    @Override
    public ClearCities setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ClearCities) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ClearCities setQuotaUser(java.lang.String quotaUser) {
      return (ClearCities) super.setQuotaUser(quotaUser);
    }

    @Override
    public ClearCities setUserIp(java.lang.String userIp) {
      return (ClearCities) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String country;

    /**

     */
    public java.lang.String getCountry() {
      return country;
    }

    public ClearCities setCountry(java.lang.String country) {
      this.country = country;
      return this;
    }

    @Override
    public ClearCities set(String parameterName, Object value) {
      return (ClearCities) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getCities".
   *
   * This request holds the parameters needed by the cityApi server.  After setting any optional
   * parameters, call the {@link GetCities#execute()} method to invoke the remote operation.
   *
   * @param country
   * @return the request
   */
  public GetCities getCities(java.lang.String country) throws java.io.IOException {
    GetCities result = new GetCities(country);
    initialize(result);
    return result;
  }

  public class GetCities extends CityApiRequest<br.com.expressobits.hbus.backend.cityApi.model.CityCollection> {

    private static final String REST_PATH = "city/{country}";

    /**
     * Create a request for the method "getCities".
     *
     * This request holds the parameters needed by the the cityApi server.  After setting any optional
     * parameters, call the {@link GetCities#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetCities#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param country
     * @since 1.13
     */
    protected GetCities(java.lang.String country) {
      super(CityApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.cityApi.model.CityCollection.class);
      this.country = com.google.api.client.util.Preconditions.checkNotNull(country, "Required parameter country must be specified.");
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
    public GetCities setAlt(java.lang.String alt) {
      return (GetCities) super.setAlt(alt);
    }

    @Override
    public GetCities setFields(java.lang.String fields) {
      return (GetCities) super.setFields(fields);
    }

    @Override
    public GetCities setKey(java.lang.String key) {
      return (GetCities) super.setKey(key);
    }

    @Override
    public GetCities setOauthToken(java.lang.String oauthToken) {
      return (GetCities) super.setOauthToken(oauthToken);
    }

    @Override
    public GetCities setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetCities) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetCities setQuotaUser(java.lang.String quotaUser) {
      return (GetCities) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetCities setUserIp(java.lang.String userIp) {
      return (GetCities) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String country;

    /**

     */
    public java.lang.String getCountry() {
      return country;
    }

    public GetCities setCountry(java.lang.String country) {
      this.country = country;
      return this;
    }

    @Override
    public GetCities set(String parameterName, Object value) {
      return (GetCities) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertCity".
   *
   * This request holds the parameters needed by the cityApi server.  After setting any optional
   * parameters, call the {@link InsertCity#execute()} method to invoke the remote operation.
   *
   * @param content the {@link br.com.expressobits.hbus.backend.cityApi.model.City}
   * @return the request
   */
  public InsertCity insertCity(br.com.expressobits.hbus.backend.cityApi.model.City content) throws java.io.IOException {
    InsertCity result = new InsertCity(content);
    initialize(result);
    return result;
  }

  public class InsertCity extends CityApiRequest<br.com.expressobits.hbus.backend.cityApi.model.City> {

    private static final String REST_PATH = "city";

    /**
     * Create a request for the method "insertCity".
     *
     * This request holds the parameters needed by the the cityApi server.  After setting any optional
     * parameters, call the {@link InsertCity#execute()} method to invoke the remote operation. <p>
     * {@link
     * InsertCity#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param content the {@link br.com.expressobits.hbus.backend.cityApi.model.City}
     * @since 1.13
     */
    protected InsertCity(br.com.expressobits.hbus.backend.cityApi.model.City content) {
      super(CityApi.this, "POST", REST_PATH, content, br.com.expressobits.hbus.backend.cityApi.model.City.class);
    }

    @Override
    public InsertCity setAlt(java.lang.String alt) {
      return (InsertCity) super.setAlt(alt);
    }

    @Override
    public InsertCity setFields(java.lang.String fields) {
      return (InsertCity) super.setFields(fields);
    }

    @Override
    public InsertCity setKey(java.lang.String key) {
      return (InsertCity) super.setKey(key);
    }

    @Override
    public InsertCity setOauthToken(java.lang.String oauthToken) {
      return (InsertCity) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertCity setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertCity) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertCity setQuotaUser(java.lang.String quotaUser) {
      return (InsertCity) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertCity setUserIp(java.lang.String userIp) {
      return (InsertCity) super.setUserIp(userIp);
    }

    @Override
    public InsertCity set(String parameterName, Object value) {
      return (InsertCity) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link CityApi}.
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

    /** Builds a new instance of {@link CityApi}. */
    @Override
    public CityApi build() {
      return new CityApi(this);
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
     * Set the {@link CityApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setCityApiRequestInitializer(
        CityApiRequestInitializer cityapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(cityapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
