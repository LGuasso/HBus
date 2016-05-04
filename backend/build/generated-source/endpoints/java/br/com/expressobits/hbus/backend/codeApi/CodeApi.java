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
 * on 2016-05-03 at 23:13:13 UTC 
 * Modify at your own risk.
 */

package br.com.expressobits.hbus.backend.codeApi;

/**
 * Service definition for CodeApi (v1).
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
 * This service uses {@link CodeApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class CodeApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the codeApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
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
  public static final String DEFAULT_SERVICE_PATH = "codeApi/v1/code/";

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
  public CodeApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  CodeApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getCode".
   *
   * This request holds the parameters needed by the codeApi server.  After setting any optional
   * parameters, call the {@link GetCode#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetCode getCode(java.lang.Long id) throws java.io.IOException {
    GetCode result = new GetCode(id);
    initialize(result);
    return result;
  }

  public class GetCode extends CodeApiRequest<br.com.expressobits.hbus.backend.codeApi.model.Code> {

    private static final String REST_PATH = "{id}";

    /**
     * Create a request for the method "getCode".
     *
     * This request holds the parameters needed by the the codeApi server.  After setting any optional
     * parameters, call the {@link GetCode#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetCode#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
     * be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetCode(java.lang.Long id) {
      super(CodeApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.codeApi.model.Code.class);
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
    public GetCode setAlt(java.lang.String alt) {
      return (GetCode) super.setAlt(alt);
    }

    @Override
    public GetCode setFields(java.lang.String fields) {
      return (GetCode) super.setFields(fields);
    }

    @Override
    public GetCode setKey(java.lang.String key) {
      return (GetCode) super.setKey(key);
    }

    @Override
    public GetCode setOauthToken(java.lang.String oauthToken) {
      return (GetCode) super.setOauthToken(oauthToken);
    }

    @Override
    public GetCode setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetCode) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetCode setQuotaUser(java.lang.String quotaUser) {
      return (GetCode) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetCode setUserIp(java.lang.String userIp) {
      return (GetCode) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public GetCode setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public GetCode set(String parameterName, Object value) {
      return (GetCode) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getCodes".
   *
   * This request holds the parameters needed by the codeApi server.  After setting any optional
   * parameters, call the {@link GetCodes#execute()} method to invoke the remote operation.
   *
   * @param country
   * @param cityName
   * @return the request
   */
  public GetCodes getCodes(java.lang.String country, java.lang.String cityName) throws java.io.IOException {
    GetCodes result = new GetCodes(country, cityName);
    initialize(result);
    return result;
  }

  public class GetCodes extends CodeApiRequest<br.com.expressobits.hbus.backend.codeApi.model.CodeCollection> {

    private static final String REST_PATH = "{country}/{cityName}";

    /**
     * Create a request for the method "getCodes".
     *
     * This request holds the parameters needed by the the codeApi server.  After setting any optional
     * parameters, call the {@link GetCodes#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetCodes#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param country
     * @param cityName
     * @since 1.13
     */
    protected GetCodes(java.lang.String country, java.lang.String cityName) {
      super(CodeApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.codeApi.model.CodeCollection.class);
      this.country = com.google.api.client.util.Preconditions.checkNotNull(country, "Required parameter country must be specified.");
      this.cityName = com.google.api.client.util.Preconditions.checkNotNull(cityName, "Required parameter cityName must be specified.");
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
    public GetCodes setAlt(java.lang.String alt) {
      return (GetCodes) super.setAlt(alt);
    }

    @Override
    public GetCodes setFields(java.lang.String fields) {
      return (GetCodes) super.setFields(fields);
    }

    @Override
    public GetCodes setKey(java.lang.String key) {
      return (GetCodes) super.setKey(key);
    }

    @Override
    public GetCodes setOauthToken(java.lang.String oauthToken) {
      return (GetCodes) super.setOauthToken(oauthToken);
    }

    @Override
    public GetCodes setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetCodes) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetCodes setQuotaUser(java.lang.String quotaUser) {
      return (GetCodes) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetCodes setUserIp(java.lang.String userIp) {
      return (GetCodes) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String country;

    /**

     */
    public java.lang.String getCountry() {
      return country;
    }

    public GetCodes setCountry(java.lang.String country) {
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

    public GetCodes setCityName(java.lang.String cityName) {
      this.cityName = cityName;
      return this;
    }

    @Override
    public GetCodes set(String parameterName, Object value) {
      return (GetCodes) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertCode".
   *
   * This request holds the parameters needed by the codeApi server.  After setting any optional
   * parameters, call the {@link InsertCode#execute()} method to invoke the remote operation.
   *
   * @param country
   * @param cityName
   * @param content the {@link br.com.expressobits.hbus.backend.codeApi.model.Code}
   * @return the request
   */
  public InsertCode insertCode(java.lang.String country, java.lang.String cityName, br.com.expressobits.hbus.backend.codeApi.model.Code content) throws java.io.IOException {
    InsertCode result = new InsertCode(country, cityName, content);
    initialize(result);
    return result;
  }

  public class InsertCode extends CodeApiRequest<br.com.expressobits.hbus.backend.codeApi.model.Code> {

    private static final String REST_PATH = "{country}/{cityName}";

    /**
     * Create a request for the method "insertCode".
     *
     * This request holds the parameters needed by the the codeApi server.  After setting any optional
     * parameters, call the {@link InsertCode#execute()} method to invoke the remote operation. <p>
     * {@link
     * InsertCode#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param country
     * @param cityName
     * @param content the {@link br.com.expressobits.hbus.backend.codeApi.model.Code}
     * @since 1.13
     */
    protected InsertCode(java.lang.String country, java.lang.String cityName, br.com.expressobits.hbus.backend.codeApi.model.Code content) {
      super(CodeApi.this, "POST", REST_PATH, content, br.com.expressobits.hbus.backend.codeApi.model.Code.class);
      this.country = com.google.api.client.util.Preconditions.checkNotNull(country, "Required parameter country must be specified.");
      this.cityName = com.google.api.client.util.Preconditions.checkNotNull(cityName, "Required parameter cityName must be specified.");
    }

    @Override
    public InsertCode setAlt(java.lang.String alt) {
      return (InsertCode) super.setAlt(alt);
    }

    @Override
    public InsertCode setFields(java.lang.String fields) {
      return (InsertCode) super.setFields(fields);
    }

    @Override
    public InsertCode setKey(java.lang.String key) {
      return (InsertCode) super.setKey(key);
    }

    @Override
    public InsertCode setOauthToken(java.lang.String oauthToken) {
      return (InsertCode) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertCode setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertCode) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertCode setQuotaUser(java.lang.String quotaUser) {
      return (InsertCode) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertCode setUserIp(java.lang.String userIp) {
      return (InsertCode) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String country;

    /**

     */
    public java.lang.String getCountry() {
      return country;
    }

    public InsertCode setCountry(java.lang.String country) {
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

    public InsertCode setCityName(java.lang.String cityName) {
      this.cityName = cityName;
      return this;
    }

    @Override
    public InsertCode set(String parameterName, Object value) {
      return (InsertCode) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link CodeApi}.
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

    /** Builds a new instance of {@link CodeApi}. */
    @Override
    public CodeApi build() {
      return new CodeApi(this);
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
     * Set the {@link CodeApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setCodeApiRequestInitializer(
        CodeApiRequestInitializer codeapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(codeapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}