package com.castores.api.utils;

public class Constants {

    private Constants() {}

    /*******************************************************************************************************/
    /***************** CODES AND MESSAGES FOR APPLICATION RESPONSES ****************************************/
    /*******************************************************************************************************/

    /************** RESPONSES 200 *************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_SUCCESS = 0;
    public static final String MSJE_READ_SUCCESS = "The service retrieved the records successfully";
    public static final String MSJE_SAVE_SUCCESS = "The service saved the record successfully";
    public static final String MSJE_UPDATE_SUCCESS = "The service updated the record successfully";
    public static final String MSJE_DELETE_SUCCESS = "The service deleted the record successfully";

    /************** RESPONSES 400 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_REQUEST_INVALID = 100;
    public static final String MSJE_ERROR_REQUEST_INVALID = "The client request is invalid";

    /************** RESPONSES 401 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_CLIENT_UNAUTHORIZED = 101;
    public static final String MSJE_ERROR_CLIENT_UNAUTHORIZED = "You are not authorized to access this resource";

    /************** RESPONSES 403 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_CLIENT_FORBIDDEN = 102;

    /************** RESPONSES 404 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_NOT_FOUND_PATH = 104;
    public static final String MSJE_ERROR_NOT_FOUND_PATH = "The requested resource was not found";

    public static final int CODE_ERROR_NOT_FOUND_INFO = 105;
    public static final String MSJE_ERROR_READ_DATABASE = "Error retrieving information";
    public static final String MSJE_NOT_FOUND_READ_DATABASE = "No information found";

    /************** RESPONSES 405 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_METHOD_NOT_ALLOWED = 106;
    public static final String MSJE_ERROR_METHOD_NOT_ALLOWED = "Method not allowed";

    /************** RESPONSES 408 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_REQUEST_TIMEOUT = 107;
    public static final String MSJE_ERROR_REQUEST_TIMEOUT = "The request did not respond within the established time";

    /************** RESPONSES 409 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_CONFLICT = 108;
    public static final String MSJE_ERROR_CONFLICT = "The entity's state does not allow the operation";

    /************** RESPONSES 422 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_UNPROCESSABLE_ENTITY = 109;
    public static final String MSJE_ERROR_UNPROCESSABLE_ENTITY = "The Content Type is not processable by the service";

    /************** RESPONSES 429 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_TOO_MANY_REQUESTS = 110;
    public static final String MSJE_ERROR_TOO_MANY_REQUESTS = "The number of allowed requests has been exceeded";

    /************** RESPONSES 500 **************************************************************************/
    /*******************************************************************************************************/
    public static final int CODE_ERROR_SERVICE_INTERNAL = 111;
    public static final String MSJE_ERROR_SERVICE_INTERNAL = "The service encountered an uncontrolled error";
    public static final String MSJE_ERROR_SAVE_DATABASE = "Error saving the information";
    public static final String MSJE_ERROR_UPDATE_DATABASE = "Error updating the information";
    public static final String MSJE_ERROR_DELETE_DATABASE = "Error deleting the information";

    public static final int CODE_ERROR_SERVICE_UNAVAILABLE = 113;
    public static final String MSJE_ERROR_SERVICE_UNAVAILABLE = "The service is unavailable";

    /*******************************************************************************************************/
    /************************* RESILIENCE4J (RETRIES, CIRCUIT BREAKERS) ************************************/
    /*******************************************************************************************************/
    public static final String RESILIENCE4J_SSO = "sso";
    public static final String RESILIENCE4J_MONGODB = "mongodb";
    public static final String RESILIENCE4J_PGSQL = "pgsql";

}