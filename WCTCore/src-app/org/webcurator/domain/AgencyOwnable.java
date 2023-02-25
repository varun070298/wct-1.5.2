/*
 *  Copyright 2006 The National Library of New Zealand
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.webcurator.domain;

import org.webcurator.domain.model.auth.Agency;

/**
 * The interface implemented by objects that are owned by an agency.
 * @author bprice
 */
public interface AgencyOwnable {
	/**
	 * Return the Agency that this object belongs to.
	 * @return the Agency that this object belongs to
	 */
    Agency getOwningAgency();
}
