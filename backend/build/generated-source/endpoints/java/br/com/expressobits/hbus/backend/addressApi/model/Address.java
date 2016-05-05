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
 * on 2016-05-04 at 00:12:49 UTC 
 * Modify at your own risk.
 */

package br.com.expressobits.hbus.backend.addressApi.model;

/**
 * Model definition for Address.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the addressApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Address extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer number;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String road;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getNumber() {
    return number;
  }

  /**
   * @param number number or {@code null} for none
   */
  public Address setNumber(java.lang.Integer number) {
    this.number = number;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getRoad() {
    return road;
  }

  /**
   * @param road road or {@code null} for none
   */
  public Address setRoad(java.lang.String road) {
    this.road = road;
    return this;
  }

  @Override
  public Address set(String fieldName, Object value) {
    return (Address) super.set(fieldName, value);
  }

  @Override
  public Address clone() {
    return (Address) super.clone();
  }

}
