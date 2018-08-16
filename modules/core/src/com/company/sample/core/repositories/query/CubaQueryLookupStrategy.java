package com.company.sample.core.repositories.query;

import com.company.sample.core.repositories.config.JpqlQuery;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.PartTree;

import java.lang.reflect.Method;

public class CubaQueryLookupStrategy implements QueryLookupStrategy {

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {

        JpqlQuery jpqlQuery = method.getDeclaredAnnotation(JpqlQuery.class);
        if (jpqlQuery != null) {
            String qryString = jpqlQuery.value();
            return new CubaJpqlQuery(method, metadata, factory, qryString);
        } else {
            PartTree qryTree = new PartTree(method.getName(), metadata.getDomainType());
            if (qryTree.isDelete()) {
                return new CubaDeleteQuery(method, metadata, factory, qryTree);
            } else {
                return new CubaListQuery(method, metadata, factory, namedQueries, qryTree);
            }
        }
    }
}
