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
require_once(DOCROOT . '/org/wrml/core/model/schema/Type.php');

class LinkRelation extends StaticSchema {

    function __construct($context) {
        parent::__construct($context);
        
        $this->setDescription("Describes the relationship between two linked resources.");
                
        $context = $this->getContext();
                
        $fields = &$this->getFields();
        
        $uriTextSyntaxConstraint = $context->createUriTextSyntaxConstraint()->getId();
                
        $baseSchemaIds = &$this->getBaseSchemaIds();
        $baseSchemaIds[] = $context->getRequestUri(Context::DOCUMENT_SCHEMA_PATH);
    }

}
