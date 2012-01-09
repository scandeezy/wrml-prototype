<?php 
/*
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
        
require_once(DOCROOT . '/runtime/StaticSchema.php');
require_once(DOCROOT . '/runtime/StaticField.php');
require_once(DOCROOT . '/org/wrml/model/schema/Type.php');

class Schema extends StaticSchema {

    function __construct($context) {
        parent::__construct($context);
        
        $this->setDescription("A schema describes the structure of a model independent of its format. Schemas provide contractual resource type definitions, which are a crucial component of the interface that binds a server and its clients together.");
                
        $context = $this->getContext();                        
                
        $baseSchemas = &$this->getBaseSchemas();
        $baseSchemas[] = $context->getRequestUri('/org/wrml/model/Document');
        
        $fields = &$this->getFields();
        
        $baseSchemasField = new StaticField("baseSchemas", Type::LIST_TYPE);
        $baseSchemasField->setDescription("The schema's base schemas. Schema extension allows a schema's forms to inherit the fields and links of its base schemas. Schema extension is analogous to the interface inheritance model offered by classical object-oriented programming languages like Java and C#.");        
        
        //$uriTextSyntaxConstraint = $context->createUriTextSyntaxConstraint()->getId();
        
        //$idFieldConstraints = &$baseSchemasField->getConstraints();        
        //$idFieldConstraints[] = $uriTextSyntaxConstraint;
        
        $fields[] = $baseSchemasField;
        
        $fieldsField = new StaticField("fields", Type::MAP_TYPE);
        $fieldsField->setDescription("The schema's field definitions.");
        
        $fields[] = $fieldsField;
        
    }
    

}
