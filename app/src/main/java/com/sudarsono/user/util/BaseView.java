
package com.sudarsono.user.util;


public interface BaseView<T> {

    void setPresenter(T presenter);

    void showGeneralError(String code, String message);

}
