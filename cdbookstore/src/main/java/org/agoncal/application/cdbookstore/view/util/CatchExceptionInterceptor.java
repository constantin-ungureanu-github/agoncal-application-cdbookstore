package org.agoncal.application.cdbookstore.view.util;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@CatchException
@Interceptor
public class CatchExceptionInterceptor {

    @Inject
    private FacesContext facesContext;

    @AroundInvoke
    private Object intercept(final InvocationContext invocationContext) throws Exception {
        try {
            return invocationContext.proceed();
        } catch (final Throwable throwable) {
            facesContext.addMessage(null, new FacesMessage(SEVERITY_ERROR, throwable.getMessage(), null));
            return null;
        }
    }
}
