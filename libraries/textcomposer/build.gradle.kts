/*
 * Copyright (c) 2022 New Vector Ltd
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

// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("io.element.android-compose-library")
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "io.element.android.libraries.textcomposer"
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":libraries:elementresources"))
    implementation(project(":libraries:ui-strings"))
    implementation(project(":libraries:core"))
    implementation(project(":libraries:matrix"))
    implementation(libs.wysiwyg)
    implementation(libs.androidx.constraintlayout)
    implementation("com.google.android.material:material:1.7.0")
    ksp(libs.showkase.processor)
}
