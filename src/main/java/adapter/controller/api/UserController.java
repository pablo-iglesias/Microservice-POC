package adapter.controller.api;


import adapter.response.model.api.ApiResponseError;
import adapter.response.model.api.ApiResponseUserCollection;
import adapter.response.model.api.ApiResponseUserResource;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import core.Server;
import core.database.Database;
import core.entity.HttpRequest;
import core.entity.HttpResponse;
import domain.constraints.UserObject;
import domain.usecase.api.*;

import java.net.HttpURLConnection;

/**
 * Controller for the User collection of the REST API
 */
public class UserController extends ApiController{

    /**
     * Get users collection
     *
     * @param request
     * @return
     * @throws Exception
     */
    protected HttpResponse GET(HttpRequest request) throws Exception
    {
        try {

            UsecaseGetUsers usecase = Server.getInstance(UsecaseGetUsers.class);

            switch(usecase.execute())
            {
                case USERS_RETRIEVED_SUCCESSFULLY:

                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_OK,
                            new ApiResponseUserCollection(
                                    usecase.getUsers(),
                                    usecase.getRoles()
                            )
                    );

                case NO_USERS_FOUND:
                default:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_OK,
                            new ApiResponseUserCollection()
                    );
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return getResponse(
                    request,
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    new ApiResponseError(e.getMessage())
            );
        }
    }

    /**
     * Get a single user resource
     *
     * @param request
     * @param refdUserId - Id of the User resource
     * @return HttpResponse
     * @throws Exception
     */
    protected HttpResponse GET(HttpRequest request, Integer refdUserId) throws Exception
    {
        try {
            UsecaseGetOneUser usecase = Server.getInstance(UsecaseGetOneUser.class);
            usecase.setRefUserId(refdUserId);

            switch(usecase.execute())
            {
                case USER_RETRIEVED_SUCCESSFULLY:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_OK,
                            new ApiResponseUserResource(
                                    usecase.getUser(),
                                    usecase.getRoles()
                            )
                    );

                case USER_NOT_FOUND:
                default:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_NOT_FOUND,
                            new ApiResponseError("User with this id does not exist")
                    );
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return getResponse(
                    request,
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    new ApiResponseError(e.getMessage())
            );
        }
    }

    /**
     * Create a new user
     *
     * @param request
     * @param authUserId - User that is creating a new user
     * @param body - Json
     * @return
     * @throws Exception
     */
    protected HttpResponse POST(HttpRequest request, Integer authUserId, String body) throws Exception
    {
        try {
            Gson gson = new Gson();
            UserObject userData = gson.fromJson(body, UserObject.class);

            Database db = Server.getDatabase();
            db.startTransaction();

            UsecaseAddNewUser usecase = Server.getInstance(UsecaseAddNewUser.class);
            usecase.setAuthUserId(authUserId);
            usecase.setUserData(userData);

            switch (usecase.execute())
            {
                case USER_CREATED_SUCCESSFULLY:
                    db.commit();
                    return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);

                case NOT_AUTHORISED:
                    return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);

                case USER_ALREADY_EXISTS:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            new ApiResponseError("User with this username already exists")
                    );

                case BAD_INPUT_DATA:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            new ApiResponseError("Insufficient data supplied, need username, password and at least one role")
                    );

                case USER_NOT_CREATED:
                default:
                    db.rollback();
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_INTERNAL_ERROR,
                            new ApiResponseError("Unknown error")
                    );
            }
        }
        catch (JsonSyntaxException e) {
            return getResponse(
                    request,
                    HttpURLConnection.HTTP_BAD_REQUEST,
                    new ApiResponseError("Json syntax")
            );
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return getResponse(
                    request,
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    new ApiResponseError(e.getMessage())
            );
        }
    }

    /**
     * Update an existing user
     *
     * @param request
     * @param authUserId - User that is updating
     * @param refUserId - Id of the User resource that is to be updated
     * @param body - Json
     * @return
     * @throws Exception
     */
    protected HttpResponse PUT(HttpRequest request, Integer authUserId, Integer refUserId, String body) throws Exception
    {
        try {
            Gson gson = new Gson();
            UserObject user = gson.fromJson(body, UserObject.class);

            Database db = Server.getDatabase();
            db.startTransaction();

            UsecaseUpdateExistingUser usecase = Server.getInstance(UsecaseUpdateExistingUser.class);
            usecase.setAuthUserId(authUserId);
            usecase.setRefUserId(refUserId);
            usecase.setUserData(user);

            switch (usecase.execute())
            {
                case USER_UPDATED_SUCCESSFULLY:
                    db.commit();
                    return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);

                case NOT_AUTHORISED:
                    return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);

                case USER_DOES_NOT_EXIST:
                    return getResponse(request, HttpURLConnection.HTTP_NOT_FOUND,
                            new ApiResponseError("User with this id does not exist")
                    );

                case USERNAME_ALREADY_TAKEN:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_CONFLICT,
                            new ApiResponseError("The specified username is already in use")
                    );

                case BAD_INPUT_DATA:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_BAD_REQUEST,
                            new ApiResponseError("Insufficient data supplied, need username, password and at least one role")
                    );

                case USER_NOT_UPDATED:
                default:
                    db.rollback();
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_INTERNAL_ERROR,
                            new ApiResponseError("Unknown error")
                    );
            }
        }
        catch (JsonSyntaxException e) {
            return getResponse(
                    request,
                    HttpURLConnection.HTTP_BAD_REQUEST,
                    new ApiResponseError("Json syntax")
            );
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return getResponse(
                    request,
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    new ApiResponseError(e.getMessage())
            );
        }
    }

    /**
     * Remove a user resource
     *
     * @param request
     * @param authUserId - User that is deleting
     * @param refUserId - Id of the User resource that is to be deleted
     * @return
     * @throws Exception
     */
    protected HttpResponse DELETE(HttpRequest request, Integer authUserId, Integer refUserId) throws Exception {

        try {
            Database db = Server.getDatabase();
            db.startTransaction();

            UsecaseDeleteOneUser usecase = Server.getInstance(UsecaseDeleteOneUser.class);
            usecase.setAuthUserId(authUserId);
            usecase.setRefUserId(refUserId);

            switch (usecase.execute())
            {
                case USER_DELETED_SUCCESSFULLY:
                    db.commit();
                    return new HttpResponse(HttpURLConnection.HTTP_NO_CONTENT);

                case NOT_AUTHORISED:
                    return new HttpResponse(HttpURLConnection.HTTP_UNAUTHORIZED);

                case USER_DOES_NOT_EXIST:
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_NOT_FOUND,
                            new ApiResponseError("User with this id does not exist")
                    );

                case USER_NOT_DELETED:
                default:
                    db.rollback();
                    return getResponse(
                            request,
                            HttpURLConnection.HTTP_INTERNAL_ERROR,
                            new ApiResponseError("Unknown error")
                    );
            }
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return getResponse(
                    request,
                    HttpURLConnection.HTTP_INTERNAL_ERROR,
                    new ApiResponseError(e.getMessage())
            );
        }
    }
}
