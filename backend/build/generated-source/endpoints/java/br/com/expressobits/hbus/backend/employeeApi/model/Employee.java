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
 * on 2016-05-03 at 23:13:03 UTC 
 * Modify at your own risk.
 */

package br.com.expressobits.hbus.backend.employeeApi.model;

/**
 * Model definition for Employee.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the employeeApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Employee extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean attendedHrTraining;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String firstName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime hireDate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String lastName;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getAttendedHrTraining() {
    return attendedHrTraining;
  }

  /**
   * @param attendedHrTraining attendedHrTraining or {@code null} for none
   */
  public Employee setAttendedHrTraining(java.lang.Boolean attendedHrTraining) {
    this.attendedHrTraining = attendedHrTraining;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName firstName or {@code null} for none
   */
  public Employee setFirstName(java.lang.String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getHireDate() {
    return hireDate;
  }

  /**
   * @param hireDate hireDate or {@code null} for none
   */
  public Employee setHireDate(com.google.api.client.util.DateTime hireDate) {
    this.hireDate = hireDate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLastName() {
    return lastName;
  }

  /**
   * @param lastName lastName or {@code null} for none
   */
  public Employee setLastName(java.lang.String lastName) {
    this.lastName = lastName;
    return this;
  }

  @Override
  public Employee set(String fieldName, Object value) {
    return (Employee) super.set(fieldName, value);
  }

  @Override
  public Employee clone() {
    return (Employee) super.clone();
  }

}
