package ru.clevertec.house.constant;


public class Constant {

    // pagination
    public static final int PAGE_SIZE = 15;
    public static final int PAGE_NUMBER = 1;

    // error
    public static final String INVALID_REQUEST_PARAMETERS_ERROR = "Invalid request parameters";

    // error House
    public static final String HOUSE_NOT_FOUND_ERROR = "House not found";
    public static final String HOUSE_RETRIEVING_ERROR = "Error retrieving houses list";
    public static final String HOUSE_CREATED_ERROR = "House created error";
    public static final String HOUSE_UPDATED_ERROR = "House updated error";
    public static final String HOUSE_DELETED_ERROR = "House deleted error";
    public static final String HOUSE_IS_NULL_ERROR = "House is null";

    // error Person
    public static final String PERSON_NOT_FOUND_ERROR = "Person not found";
    public static final String PERSON_RETRIEVING_ERROR = "Error retrieving persons list";
    public static final String PERSON_CREATED_ERROR = "Person created error";
    public static final String PERSON_UPDATED_ERROR = "Person updated error";
    public static final String PERSON_DELETED_ERROR = "Person deleted error";

    // message
    public static final String NO_SUCH_DATA_MESSAGE = "No such data";

    // message House
    public static final String HOUSE_IS_DELETED_MESSAGE = "House is deleted";

    // message Person
    public static final String PERSON_IS_DELETED_MESSAGE = "Person is deleted";
}
