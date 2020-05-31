package com.example.dps_application.api

import com.google.gson.FieldAttributes
import com.google.gson.ExclusionStrategy


class AnnotationExclusionStrategy : ExclusionStrategy {

    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(Exclude::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }
}