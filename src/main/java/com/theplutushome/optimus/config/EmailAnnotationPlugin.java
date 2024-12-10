package com.theplutushome.optimus.config;

import jakarta.validation.constraints.Email;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.Optional;


//@Component
//@Order(Validators.BEAN_VALIDATOR_PLUGIN_ORDER)
//public class EmailAnnotationPlugin implements ModelPropertyBuilderPlugin {
////    @Override
////    public boolean supports(DocumentationType delimiter) {
////        return true;
////    }
////
////    @Override
////    public void apply(ModelPropertyContext context) {
////        Optional<Email> email = annotationFromBean(context, Email.class);
////        if (email.isPresent()) {
////            context.getSpecificationBuilder().facetBuilder(StringElementFacetBuilder.class)
////                    .pattern(email.get().regexp());
////            context.getSpecificationBuilder().example("email@email.com");
////        }
////    }
//}