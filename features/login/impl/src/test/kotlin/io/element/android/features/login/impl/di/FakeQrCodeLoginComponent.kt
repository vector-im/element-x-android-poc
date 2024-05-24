/*
 * Copyright (c) 2024 New Vector Ltd
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

package io.element.android.features.login.impl.di

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.plugin.Plugin
import io.element.android.features.login.impl.qrcode.FakeQrCodeLoginManager
import io.element.android.features.login.impl.qrcode.QrCodeLoginFlowNode
import io.element.android.features.login.impl.qrcode.QrCodeLoginManager
import io.element.android.libraries.architecture.AssistedNodeFactory
import io.element.android.libraries.architecture.createNode

internal class FakeQrCodeLoginComponent(private val _qrCodeLoginManager: QrCodeLoginManager) :
    QrCodeLoginComponent {
    // Ignore this error, it does override a method once code generation is done
    override fun qrCodeLoginManager(): QrCodeLoginManager = _qrCodeLoginManager

    class Builder(private val qrCodeLoginManager: QrCodeLoginManager = FakeQrCodeLoginManager()) :
        QrCodeLoginComponent.Builder {
        override fun build(): QrCodeLoginComponent {
            return FakeQrCodeLoginComponent(qrCodeLoginManager)
        }
    }

    override fun nodeFactories(): Map<Class<out Node>, AssistedNodeFactory<*>> {
        return mapOf(
            QrCodeLoginFlowNode::class.java to object : AssistedNodeFactory<QrCodeLoginFlowNode> {
                override fun create(buildContext: BuildContext, plugins: List<Plugin>): QrCodeLoginFlowNode {
                    return createNode<QrCodeLoginFlowNode>(buildContext, plugins)
                }
            }
        )
    }
}
