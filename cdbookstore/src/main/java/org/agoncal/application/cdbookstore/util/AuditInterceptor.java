package org.agoncal.application.cdbookstore.util;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;

@Auditable
@Interceptor
public class AuditInterceptor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private Logger logger;

    @AroundInvoke
    private Object intercept(final InvocationContext invocationContext) throws Exception {
        final long begin = System.currentTimeMillis();
        try {
            return invocationContext.proceed();
        } finally {
            logger.info("Took " + (System.currentTimeMillis() - begin));
        }
    }
}
