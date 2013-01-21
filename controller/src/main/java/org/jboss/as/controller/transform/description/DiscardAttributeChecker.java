/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.controller.transform.description;

import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.dmr.ModelNode;

/**
 * Checks whether an attribute should be discarded or not
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public interface DiscardAttributeChecker {

    /**
     * Returns {@code true} if the attribute should be discarded if expressions are used
     *
     * @return whether to discard if exressions are used
     */
    boolean isDiscardExpressions();

    /**
     * Returns {@code true} if the attribute should be discarded if it is undefined
     *
     * @return whether to discard if the attribute is undefined
     */
    boolean isDiscardUndefined();

    boolean isOperationParameterDiscardable(String attributeName, ModelNode attributeValue, ModelNode operation, TransformationContext context);

    boolean isResourceAttributeDiscardable(String attributeName, ModelNode attributeValue, TransformationContext context);

    abstract class DefaultDiscardAttributeChecker implements DiscardAttributeChecker {
        protected final boolean discardExpressions;
        protected final boolean discardUndefined;

        /**
         * Constructor
         *
         * @param discardExpressions {@code true} if the attribute should be discarded if expressions are used
         * @param discardUndefined {@code true} if the attribute should be discarded if expressions are used
         */
        public DefaultDiscardAttributeChecker(final boolean discardExpressions, final boolean discardUndefined) {
            this.discardExpressions = discardExpressions;
            this.discardUndefined = discardUndefined;
        }

        /**
         * Constructor.
         * Sets it up with {@code discardExpressions==true} and {@code discardUndefined==true}
         *
         */
        public DefaultDiscardAttributeChecker() {
            this(false, true);
        }

        /**
         * Returns {@code true} if the attribute should be discarded if expressions are used
         *
         * @return whether to discard if exressions are used
         */
        public boolean isDiscardExpressions() {
            return discardExpressions;
        }

        /**
         * Returns {@code true} if the attribute should be discarded if it is undefined
         *
         * @return whether to discard if the attribute is undefined
         */
        public boolean isDiscardUndefined() {
            return discardUndefined;
        }

        @Override
        public boolean isOperationParameterDiscardable(String attributeName, ModelNode attributeValue, ModelNode operation, TransformationContext context) {
            return isValueDiscardable(attributeName, attributeValue, context);
        }

        @Override
        public boolean isResourceAttributeDiscardable(String attributeName, ModelNode attributeValue, TransformationContext context) {
            return isValueDiscardable(attributeName, attributeValue, context);
        }

        /**
         * Returns {@code true} if the attribute should be discarded.
         *
         * @param attributeName the name of the attribute
         * @param attributeValue the value of the attribute
         * @param context the TransformationContext
         * @return whether to discard if exressions are used
         */
        protected abstract boolean isValueDiscardable(String attributeName, ModelNode attributeValue, TransformationContext context);
    }

    /**
     * A standard checker which will discard the attribute always.
     */
    DiscardAttributeChecker ALWAYS = new DefaultDiscardAttributeChecker(true, true) {

        @Override
        public boolean isValueDiscardable(String attributeName, ModelNode attributeValue, TransformationContext context) {
            return true;
        }
    };

    /**
     * A standard checker which will discard the attribute if it is undefined, as long as it is not an expressions
     */
    DiscardAttributeChecker UNDEFINED = new DefaultDiscardAttributeChecker(false, true) {

        @Override
        public boolean isValueDiscardable(String attributeName, ModelNode attributeValue, TransformationContext context) {
            return false;
        }
    };

}
