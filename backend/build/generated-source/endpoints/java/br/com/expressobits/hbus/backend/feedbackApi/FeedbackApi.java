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
 * on 2016-05-03 at 23:13:15 UTC 
 * Modify at your own risk.
 */

package br.com.expressobits.hbus.backend.feedbackApi;

/**
 * Service definition for FeedbackApi (v1).
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
 * This service uses {@link FeedbackApiRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class FeedbackApi extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.21.0 of the feedbackApi library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
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
  public static final String DEFAULT_SERVICE_PATH = "feedbackApi/v1/";

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
  public FeedbackApi(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  FeedbackApi(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "getFeedback".
   *
   * This request holds the parameters needed by the feedbackApi server.  After setting any optional
   * parameters, call the {@link GetFeedback#execute()} method to invoke the remote operation.
   *
   * @param id
   * @return the request
   */
  public GetFeedback getFeedback(java.lang.Long id) throws java.io.IOException {
    GetFeedback result = new GetFeedback(id);
    initialize(result);
    return result;
  }

  public class GetFeedback extends FeedbackApiRequest<br.com.expressobits.hbus.backend.feedbackApi.model.Feedback> {

    private static final String REST_PATH = "feedback/{id}";

    /**
     * Create a request for the method "getFeedback".
     *
     * This request holds the parameters needed by the the feedbackApi server.  After setting any
     * optional parameters, call the {@link GetFeedback#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetFeedback#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param id
     * @since 1.13
     */
    protected GetFeedback(java.lang.Long id) {
      super(FeedbackApi.this, "GET", REST_PATH, null, br.com.expressobits.hbus.backend.feedbackApi.model.Feedback.class);
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
    public GetFeedback setAlt(java.lang.String alt) {
      return (GetFeedback) super.setAlt(alt);
    }

    @Override
    public GetFeedback setFields(java.lang.String fields) {
      return (GetFeedback) super.setFields(fields);
    }

    @Override
    public GetFeedback setKey(java.lang.String key) {
      return (GetFeedback) super.setKey(key);
    }

    @Override
    public GetFeedback setOauthToken(java.lang.String oauthToken) {
      return (GetFeedback) super.setOauthToken(oauthToken);
    }

    @Override
    public GetFeedback setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetFeedback) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetFeedback setQuotaUser(java.lang.String quotaUser) {
      return (GetFeedback) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetFeedback setUserIp(java.lang.String userIp) {
      return (GetFeedback) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Long id;

    /**

     */
    public java.lang.Long getId() {
      return id;
    }

    public GetFeedback setId(java.lang.Long id) {
      this.id = id;
      return this;
    }

    @Override
    public GetFeedback set(String parameterName, Object value) {
      return (GetFeedback) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "insertFeedback".
   *
   * This request holds the parameters needed by the feedbackApi server.  After setting any optional
   * parameters, call the {@link InsertFeedback#execute()} method to invoke the remote operation.
   *
   * @param content the {@link br.com.expressobits.hbus.backend.feedbackApi.model.Feedback}
   * @return the request
   */
  public InsertFeedback insertFeedback(br.com.expressobits.hbus.backend.feedbackApi.model.Feedback content) throws java.io.IOException {
    InsertFeedback result = new InsertFeedback(content);
    initialize(result);
    return result;
  }

  public class InsertFeedback extends FeedbackApiRequest<br.com.expressobits.hbus.backend.feedbackApi.model.Feedback> {

    private static final String REST_PATH = "feedback";

    /**
     * Create a request for the method "insertFeedback".
     *
     * This request holds the parameters needed by the the feedbackApi server.  After setting any
     * optional parameters, call the {@link InsertFeedback#execute()} method to invoke the remote
     * operation. <p> {@link InsertFeedback#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param content the {@link br.com.expressobits.hbus.backend.feedbackApi.model.Feedback}
     * @since 1.13
     */
    protected InsertFeedback(br.com.expressobits.hbus.backend.feedbackApi.model.Feedback content) {
      super(FeedbackApi.this, "POST", REST_PATH, content, br.com.expressobits.hbus.backend.feedbackApi.model.Feedback.class);
    }

    @Override
    public InsertFeedback setAlt(java.lang.String alt) {
      return (InsertFeedback) super.setAlt(alt);
    }

    @Override
    public InsertFeedback setFields(java.lang.String fields) {
      return (InsertFeedback) super.setFields(fields);
    }

    @Override
    public InsertFeedback setKey(java.lang.String key) {
      return (InsertFeedback) super.setKey(key);
    }

    @Override
    public InsertFeedback setOauthToken(java.lang.String oauthToken) {
      return (InsertFeedback) super.setOauthToken(oauthToken);
    }

    @Override
    public InsertFeedback setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (InsertFeedback) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public InsertFeedback setQuotaUser(java.lang.String quotaUser) {
      return (InsertFeedback) super.setQuotaUser(quotaUser);
    }

    @Override
    public InsertFeedback setUserIp(java.lang.String userIp) {
      return (InsertFeedback) super.setUserIp(userIp);
    }

    @Override
    public InsertFeedback set(String parameterName, Object value) {
      return (InsertFeedback) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link FeedbackApi}.
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

    /** Builds a new instance of {@link FeedbackApi}. */
    @Override
    public FeedbackApi build() {
      return new FeedbackApi(this);
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
     * Set the {@link FeedbackApiRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setFeedbackApiRequestInitializer(
        FeedbackApiRequestInitializer feedbackapiRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(feedbackapiRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
