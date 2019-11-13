/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amplifyframework.api.aws;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.MutationType;
import com.amplifyframework.api.graphql.QueryType;
import com.amplifyframework.api.graphql.SubscriptionType;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelField;
import com.amplifyframework.core.model.ModelSchema;
import com.amplifyframework.core.model.query.predicate.FilteringPredicate;
import com.amplifyframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts provided model or class type into a request container
 * with automatically generated GraphQL documents that follow
 * AppSync specifications.
 */
final class AppSyncGraphQLRequestFactory {
    // This class should not be instantiated
    private AppSyncGraphQLRequestFactory(){}

    public static <T extends Model> GraphQLRequest<T> buildQuery(
            Class<T> modelClass,
            FilteringPredicate<T> predicate,
            QueryType type
    ) throws AmplifyException {
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Model> GraphQLRequest<T> buildMutation(
            T model,
            FilteringPredicate<T> predicate,
            MutationType type
    ) throws AmplifyException {
        // model is of type T so this is a safe cast - hence the warning suppression
        Class<T> modelClass = (Class<T>) model.getClass();

        StringBuilder doc = new StringBuilder();
        ModelSchema schema = ModelSchema.fromModelClass(modelClass);
        String typeStr = type.toString();
        String modelName = schema.getTargetModelName();

        doc.append("mutation ");
        doc.append(StringUtils.capitalize(typeStr) +
                StringUtils.capitalize(modelName));
        doc.append("($input: ");
        doc.append(StringUtils.capitalize(typeStr) +
                StringUtils.capitalize(modelName) +
                "Input!");
        doc.append(") { ");
        doc.append(typeStr.toLowerCase() +
                StringUtils.capitalize(modelName));
        doc.append("(input: $input) { ");

        doc.append(getModelFields(schema));

        doc.append("}}");

        Map<String, Object> input = new HashMap<>();
        input.put("input", schema.getValuesFromInstance(model));

        GraphQLRequest<T> result = new GraphQLRequest<T>(
                doc.toString(),
                input,
                modelClass
        );

        return result;
    }

    public static <T extends Model> GraphQLRequest<T> buildSubscription(
            Class<T> modelClass,
            FilteringPredicate<T> predicate,
            SubscriptionType type
    ) throws AmplifyException {
        return null;
    }

    private static String getModelFields(ModelSchema schema) {
        StringBuilder result = new StringBuilder();
        List<ModelField> sortedFields = schema.getSortedFields();

        for(int i = 0; i < sortedFields.size(); i++) {
            result.append(sortedFields.get(i).getTargetName());

            if (i < sortedFields.size()-1) {
                result.append(" ");
            }
        }

        return result.toString();
    }
}