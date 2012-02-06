/**
 * Copyright (C) 2011 WRML.org <mark@wrml.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wrml.core.runtime;

import java.net.URI;
import java.util.Stack;

import org.wrml.core.Model;

/**
 * A reflective graph-like structure that mirrors a model/field initialization
 * stream.
 */
public final class ModelGraph extends RuntimeObject {

    public final static String ROOT_RELATIONSHIP_NAME = "root";

    private Model _Root;

    /**
     * The init cursor is a visual concept that follows the "horizontal" scoping
     * of nested models. Picture the root model as the outermost object/element
     * in a JSON/XML file with pretty printing. Now imagine reading that file
     * line by line, or more accurately model by model and field by field. In
     * JSON terms, the init cursor follows along with the opening ('{' and '[')
     * and closing ('}' and ']') of nested models in order to simplify the
     * application of initial field values to serialized models in a
     * generalized, iterative way.
     */
    private final Stack<Model> _InitCursor;
    private String _InitCursorFocusRelationShipName;
    private boolean _Initialized;

    private int _MaxDepth;
    private int _CurrentDepth;

    public ModelGraph(Context context) {
        super(context);
        _InitCursor = new Stack<Model>();
        _InitCursorFocusRelationShipName = ROOT_RELATIONSHIP_NAME;
    }

    public int getCurrentDepth() {
        return _CurrentDepth;
    }

    public Model getInitCursorFocus() {

        if (_InitCursor.isEmpty()) {
            return null;
        }

        return _InitCursor.peek();
    }

    public String getInitCursorFocusRelationShipName() {
        return _InitCursorFocusRelationShipName;
    }

    public int getMaxDepth() {
        return _MaxDepth;
    }

    public Model getRoot() {
        return _Root;
    }

    public boolean isInitCursorFocused() {
        return !_InitCursor.isEmpty();
    }

    public boolean isInitialized() {
        return _Initialized;
    }

    public Model popInitCursorBack() {

        if (isInitialized()) {
            final String message = "Bug: This Model Graph is complete : " + this;
            System.err.println(message);
            throw new IllegalStateException(message);
        }

        // TODO: Modify/close the cool graph as needed

        if (_InitCursor.isEmpty()) {
            return null;
        }

        final Model blur = _InitCursor.pop();
        _CurrentDepth--;
        _Initialized = _InitCursor.isEmpty();

        final Model parentModel = getInitCursorFocus();
        final URI parentSchemaId = (parentModel != null) ? parentModel.getSchemaId() : null;
        final String parentModelValueString = (parentSchemaId != null) ? " (" + parentModel.hashCode()
                + ") { schema : \"" + parentSchemaId + "\" }" : null;

        if (isInitialized()) {
            System.out.print("W");
            System.out.print(".");
            System.out.print("R");
            System.out.print(".");
            System.out.print("M");
            System.out.print(".");
            System.out.print("L");
            System.out.print(".");
            System.out.println(" ----> Initialized a Model Graph (" + this.hashCode() + ") <--- new Model ("
                    + blur.hashCode() + ") { schema : \"" + blur.getSchemaId() + "\", name : \""
                    + blur.getFieldValue("name") + "\", parentModel : " + parentModelValueString + " }");
        }
        else {
            System.out.println("Model Graph (" + this.hashCode() + ") <--- new Model (" + blur.hashCode()
                    + ") { schema : \"" + blur.getSchemaId() + "\", name : \"" + blur.getFieldValue("name")
                    + "\", parentModel : " + parentModelValueString + " }");

        }

        return blur;
    }

    public void pushInitCursorIn(Model newFocusModel) {

        if (isInitialized()) {
            final String message = "Bug: This Model Graph is complete : " + this;
            System.err.println(message);
            throw new IllegalStateException(message);
        }

        final Model parentModel = getInitCursorFocus();

        if (parentModel == null) {
            if (_Root == null) {
                _Root = newFocusModel;
            }
            else {
                throw new IllegalStateException("Bug: Lost track of the Model Graph's initialization cursor.");
            }
        }

        _InitCursor.push(newFocusModel);
        _CurrentDepth++;
        if (_CurrentDepth > _MaxDepth) {
            _MaxDepth = _CurrentDepth;
        }

        // TODO: Build a reflective graph-like structure that mirrors the model/field stream.
        final String relationshipName = getInitCursorFocusRelationShipName();

        final URI parentSchemaId = (parentModel != null) ? parentModel.getSchemaId() : null;
        final String parentModelValueString = (parentSchemaId != null) ? " (" + parentModel.hashCode()
                + ") { schema : \"" + parentSchemaId + "\" }" : null;

        System.out.println("Model Graph (" + this.hashCode() + ") ----> new Model (" + newFocusModel.hashCode()
                + ") { schema : \"" + newFocusModel.getSchemaId() + "\", name : \""
                + newFocusModel.getFieldValue("name") + "\", id : \"" + newFocusModel.getFieldValue("id")
                + "\", rel : \"" + relationshipName + "\", parentModel : " + parentModelValueString + " }");

    }

    public void setInitCursorFocusRelationShipName(String initCursorFocusRelationShipName) {
        _InitCursorFocusRelationShipName = initCursorFocusRelationShipName;
    }

    @Override
    public String toString() {

        final Model initCursorFocus = getInitCursorFocus();
        final URI initCursorFocusSchemaId = (initCursorFocus != null) ? initCursorFocus.getSchemaId() : null;
        final String initCursorFocusValueString = (initCursorFocusSchemaId != null) ? " (" + initCursorFocus.hashCode()
                + ") { schema : \"" + initCursorFocusSchemaId + "\", name : \"" + initCursorFocus.getFieldValue("name")
                + "\" }" : null;

        return getClass().getName() + "(" + this.hashCode() + ") { initCursorFocus : " + initCursorFocusValueString
                + ", currentDepth : " + getCurrentDepth() + ", maxDepth : " + getMaxDepth() + ", initialized : "
                + _Initialized + " }";
    }

}
