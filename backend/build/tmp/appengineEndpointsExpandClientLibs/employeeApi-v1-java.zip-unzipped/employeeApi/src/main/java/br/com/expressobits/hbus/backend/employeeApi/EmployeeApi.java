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
 * on 2016-05-04 at 00:12:48 UTC 
 * Modify at your own risk.
 */

package br.com.expressobits.hbus.backend.employeeApi;

/**
 * Service definition for EmployeeApi (v1).
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
 * This service uses {@link EmployeeApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class EmployeeApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the employeeApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
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
  public static final String DEFAULT_SERVICE_PATH = "employeeApi/v1/";

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
  public EmployeeApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  EmployeeApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getEmployee".
   *
   * This request holds the parameters needed by the employeeApi server.  After setting any optional
   * parameters, call the {@link GetEmployee#execute()} method to invoke the remote operation.
   *
   * @param name
   * @return the request
   */
  public GetEmployee getEmployee(java.lang.String name) throws java.io.IOException {
    GetEmployee result = new GetEmployee(name);
    initialize(result);
    return result;
  }

  public class GetEmployee extends EmployeeApiRequest<br.com.expressobits.hbus.backend.employeeApi.model.Employee> {

    private static final String REST_PATH = "employee/{name}";

    /**
     * Create a request for the method "getEmployee".
     *
     * This request holds the parameters needed by the the employeeApi server.  After setting any
     * optional parameters, call the {@link GetEmployee#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetEmployee#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param name
     * @since 1.13
     */
    protected GetEmployee(java.lang.String name) {
      super(EmployeeApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.employeeApi.model.Employee.class);
      this.name = com.google.api.client.util.Preconditions.checkNotNull(name, "Required parameter name must be specified.");
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
    public GetEmployee setAlt(java.lang.String alt) {
      return (GetEmployee) super.setAlt(alt);
    }

    @Override
    public GetEmployee setFields(java.lang.String fields) {
      return (GetEmployee) super.setFields(fields);
    }

    @Override
    public GetEmployee setKey(java.lang.String key) {
      return (GetEmployee) super.setKey(key);
    }

    @Override
    public GetEmployee setOauthToken(java.lang.String oauthToken) {
      return (GetEmployee) super.setOauthToken(oauthToken);
    }

    @Override
    public GetEmployee setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetEmployee) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetEmployee setQuotaUser(java.lang.String quotaUser) {
      return (GetEmployee) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetEmployee setUserIp(java.lang.String userIp) {
      return (GetEmployee) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String name;

    /**

     */
    public java.lang.String getName() {
      return name;
    }

    public GetEmployee setName(java.lang.String name) {
      this.name = name;
      return this;
    }

    @Override
    public GetEmployee set(String parameterName, Object value) {
      return (GetEmployee) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getEmployees".
   *
   * This request holds the parameters needed by the employeeApi server.  After setting any optional
   * parameters, call the {@link GetEmployees#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public GetEmployees getEmployees() throws java.io.IOException {
    GetEmployees result = new GetEmployees();
    initialize(result);
    return result;
  }

  public class GetEmployees extends EmployeeApiRequest<br.com.expressobits.hbus.backend.employeeApi.model.EmployeeCollection> {

    private static final String REST_PATH = "employee";

    /**
     * Create a request for the method "getEmployees".
     *
     * This request holds the parameters needed by the the employeeApi server.  After setting any
     * optional parameters, call the {@link GetEmployees#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetEmployees#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @since 1.13
     */
    protected GetEmployees() {
      super(EmployeeApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.employeeApi.model.EmployeeCollection.class);
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
    public GetEmployees setAlt(java.lang.String alt) {
      return (GetEmployees) super.setAlt(alt);
    }

    @Override
    public GetEmployees setFields(java.lang.String fields) {
      return (GetEmployees) super.setFields(fields);
    }

    @Override
    public GetEmployees setKey(java.lang.String key) {
      return (GetEmployees) super.setKey(key);
    }

    @Override
    public GetEmployees setOauthToken(java.lang.String oauthToken) {
      return (GetEmployees) super.setOauthToken(oauthToken);
    }

    @Override
    public GetEmployees setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetEmployees) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetEmployees setQuotaUser(java.lang.String quotaUser) {
      return (GetEmployees) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetEmployees setUserIp(java.lang.String userIp) {
      return (GetEmployees) super.setUserIp(userIp);
    }

    @Override
    public GetEmployees set(String parameterName, Object value) {
      return (GetEmployees) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertEmployee".
   *
   * This request holds the parameters needed by the employeeApi server.  After setting any optional
   * parameters, call the {@link InsertEmployee#execute()} method to invoke the remote operation.
   *
   * @param content the {@link br.com.expressobits.hbus.backend.employeeApi.model.Employee}
   * @return the request
   */
  public InsertEmployee insertEmployee(br.com.expressobits.hbus.backend.employeeApi.model.Employee content) throws java.io.IOException {
    InsertEmployee result = new InsertEmployee(content);
    initialize(result);
    return result;
  }

  public class InsertEmployee extends EmployeeApiRequest<br.com.expressobits.hbus.backend.employeeApi.model.Employee> {

    private static final String REST_PATH = "employee";

    /**
     * Create a request for the method "insertEmployee".
     *
     * This request holds the parameters needed by the the employeeApi server.  After setting any
     * optional parameters, call the {@link InsertEmployee#execute()} method to invoke the remote
     * operation. <p> {@link InsertEmployee#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link br.com.expressobits.hbus.backend.employeeApi.model.Employee}
     * @since 1.13
     */
    protected InsertEmployee(br.com.expressobits.hbus.backend.employeeApi.model.Employee content) {
      super(EmployeeApi.this, "POST", REST_PATH, content, br.com.expressobits.hbus.backend.employeeApi.model.Employee.class);
    }

    @Override
    public InsertEmployee setAlt(java.lang.String alt) {
      return (InsertEmployee) super.setAlt(alt);
    }

    @Override
    public InsertEmployee setFields(java.lang.String fields) {
      return (InsertEmployee) super.setFields(fields);
    }

    @Override
    public InsertEmployee setKey(java.lang.String key) {
      return (InsertEmployee) super.setKey(key);
    }

    @Override
    public InsertEmployee setOauthToken(java.lang.String oauthToken) {
      return (InsertEmployee) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertEmployee setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertEmployee) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertEmployee setQuotaUser(java.lang.String quotaUser) {
      return (InsertEmployee) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertEmployee setUserIp(java.lang.String userIp) {
      return (InsertEmployee) super.setUserIp(userIp);
    }

    @Override
    public InsertEmployee set(String parameterName, Object value) {
      return (InsertEmployee) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "searchEmployee".
   *
   * This request holds the parameters needed by the employeeApi server.  After setting any optional
   * parameters, call the {@link SearchEmployee#execute()} method to invoke the remote operation.
   *
   * @param name
   * @return the request
   */
  public SearchEmployee searchEmployee(java.lang.String name) throws java.io.IOException {
    SearchEmployee result = new SearchEmployee(name);
    initialize(result);
    return result;
  }

  public class SearchEmployee extends EmployeeApiRequest<br.com.expressobits.hbus.backend.employeeApi.model.Employee> {

    private static final String REST_PATH = "employee/{name}";

    /**
     * Create a request for the method "searchEmployee".
     *
     * This request holds the parameters needed by the the employeeApi server.  After setting any
     * optional parameters, call the {@link SearchEmployee#execute()} method to invoke the remote
     * operation. <p> {@link SearchEmployee#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param name
     * @since 1.13
     */
    protected SearchEmployee(java.lang.String name) {
      super(EmployeeApi.this, "POST", REST_PATH, null, br.com.expressobits.hbus.backend.employeeApi.model.Employee.class);
      this.name = com.google.api.client.util.Preconditions.checkNotNull(name, "Required parameter name must be specified.");
    }

    @Override
    public SearchEmployee setAlt(java.lang.String alt) {
      return (SearchEmployee) super.setAlt(alt);
    }

    @Override
    public SearchEmployee setFields(java.lang.String fields) {
      return (SearchEmployee) super.setFields(fields);
    }

    @Override
    public SearchEmployee setKey(java.lang.String key) {
      return (SearchEmployee) super.setKey(key);
    }

    @Override
    public SearchEmployee setOauthToken(java.lang.String oauthToken) {
      return (SearchEmployee) super.setOauthToken(oauthToken);
    }

    @Override
    public SearchEmployee setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (SearchEmployee) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public SearchEmployee setQuotaUser(java.lang.String quotaUser) {
      return (SearchEmployee) super.setQuotaUser(quotaUser);
    }

    @Override
    public SearchEmployee setUserIp(java.lang.String userIp) {
      return (SearchEmployee) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String name;

    /**

     */
    public java.lang.String getName() {
      return name;
    }

    public SearchEmployee setName(java.lang.String name) {
      this.name = name;
      return this;
    }

    @Override
    public SearchEmployee set(String parameterName, Object value) {
      return (SearchEmployee) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link EmployeeApi}.
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

    /** Builds a new instance of {@link EmployeeApi}. */
    @Override
    public EmployeeApi build() {
      return new EmployeeApi(this);
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
     * Set the {@link EmployeeApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setEmployeeApiRequestInitializer(
        EmployeeApiRequestInitializer employeeapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(employeeapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
