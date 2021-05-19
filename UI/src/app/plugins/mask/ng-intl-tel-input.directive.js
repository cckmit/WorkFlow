angular.module('ngIntlTelInput')
    .directive('ngIntlTelInput', ['ngIntlTelInput', '$log',
        function(ngIntlTelInput, $log) {
            return {
                restrict: 'A',
                require: 'ngModel',
                link: function(scope, elm, attr, ctrl) {
                    // Warning for bad directive usage.
                    if (attr.type !== 'text' || elm[0].tagName !== 'INPUT') {
                        $log.warn('ng-intl-tel-input can only be applied to a *text* input');
                        return;
                    }
                    // Override default country.
                    if (attr.defaultCountry) {
                        ngIntlTelInput.set({ defaultCountry: attr.defaultCountry });
                    }
                    // Initialize.
                    ngIntlTelInput.init(elm);
                    // Validation.
                    ctrl.$validators.ngIntlTelInput = function(value) {
                        return elm.intlTelInput("isValidNumber");
                    };
                    // Set model value to valid, formatted version.
                    ctrl.$parsers.push(function(value) {
                        return elm.intlTelInput('getNumber').replace(/[^\d]/, '');
                    });
                    // Set input value to model value and trigger evaluation.
                    ctrl.$formatters.push(function(value) {
                        if (value) {
                            if (value.charAt(0) !== '+') {
                                value = '+' + value;
                            }
                            elm.intlTelInput('setNumber', value);
                            // value is in +{country code} {phone number}
                            // Since we are showing flags, we change to national number.
                            // i.e. remove +{country code}
                            if (!attr.showCountryCode) {
                                var nationalNo = elm.intlTelInput("getNumber", intlTelInputUtils.numberFormat.NATIONAL);
                                value = nationalNo;
                            }

                            // other formats for testing.
                            abc = elm.intlTelInput("getNumber", intlTelInputUtils.numberFormat.E164);

                            abc = elm.intlTelInput("getNumber", intlTelInputUtils.numberFormat.INTERNATIONAL);

                            abc = elm.intlTelInput("getNumber", intlTelInputUtils.numberFormat.RFC3966);
                        }
                        return value;
                    });
                }
            };
        }
    ]);