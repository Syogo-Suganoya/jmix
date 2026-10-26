/*
 * Copyright 2026 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package component.datepicker

import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.dom.Element
import io.jmix.core.Messages
import io.jmix.flowui.UiComponents
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import test_support.spec.FlowuiTestSpecification

import java.time.LocalDate

@SpringBootTest
class DateTimePickerUnparseableValueTest extends FlowuiTestSpecification {

    @Autowired
    UiComponents uiComponents

    @Autowired
    Messages messages

    def "DateTimePicker shows a validation error when the date input is unparseable"() {
        given: "A DateTimePicker without a value"
        def dateTimePicker = uiComponents.create(TypedDateTimePicker)

        when: "The nested date picker has an input value that cannot be parsed"
        getNestedPicker(dateTimePicker, "vaadin-date-picker").setProperty("_inputElementValue", "abc")
        dateTimePicker.fieldDelegate.checkUnparseableValue()

        then: "The component is invalid and shows the unparseable value message"
        dateTimePicker.invalid
        dateTimePicker.errorMessage == messages.getMessage("validation.unparseableValue")
    }

    def "DateTimePicker shows a validation error when the time input is unparseable"() {
        given: "A DateTimePicker without a value"
        def dateTimePicker = uiComponents.create(TypedDateTimePicker)

        when: "The nested time picker has an input value that cannot be parsed"
        getNestedPicker(dateTimePicker, "vaadin-time-picker").setProperty("_inputElementValue", "abc")
        dateTimePicker.fieldDelegate.checkUnparseableValue()

        then: "The component is invalid and shows the unparseable value message"
        dateTimePicker.invalid
        dateTimePicker.errorMessage == messages.getMessage("validation.unparseableValue")
    }

    def "DateTimePicker does not report a parsed date with an empty time as unparseable"() {
        given: "A DateTimePicker without a value"
        def dateTimePicker = uiComponents.create(TypedDateTimePicker)

        when: "Only the date is entered"
        def datePicker = getNestedPicker(dateTimePicker, "vaadin-date-picker")
        (datePicker.component.get() as DatePicker).value = LocalDate.of(2026, 1, 1)
        datePicker.setProperty("_inputElementValue", "1/1/2026")
        dateTimePicker.fieldDelegate.checkUnparseableValue()

        then: "The component is not marked invalid as unparseable"
        !dateTimePicker.invalid
    }

    private static Element getNestedPicker(TypedDateTimePicker dateTimePicker, String tag) {
        return dateTimePicker.element.children
                .filter { it.tag == tag }
                .findFirst()
                .orElseThrow()
    }
}
