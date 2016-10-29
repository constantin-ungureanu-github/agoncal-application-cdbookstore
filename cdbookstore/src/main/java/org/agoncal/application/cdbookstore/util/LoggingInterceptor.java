package org.agoncal.application.cdbookstore.util;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@Loggable
@Interceptor
public class LoggingInterceptor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private Logger logger;

    @AroundInvoke
    private Object intercept(final InvocationContext invocationContext) throws Exception {
        logger.info("{}.{}", invocationContext.getTarget().getClass().getName(),  invocationContext.getMethod().getName());
        try {
            return invocationContext.proceed();
        } finally {
            logger.info("{}.{}", invocationContext.getTarget().getClass().getName(),  invocationContext.getMethod().getName());
        }
    }
}
