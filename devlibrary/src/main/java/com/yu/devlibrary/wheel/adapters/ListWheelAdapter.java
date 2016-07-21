/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yu.devlibrary.wheel.adapters;

import android.content.Context;

import java.util.List;

/**
 * The simple Array wheel adapter
 *
 * @param <T> the element type
 */
public class ListWheelAdapter<T> extends AbstractWheelTextAdapter {

    private List<T> data;

    /**
     * Constructor
     *
     * @param context the current context
     * @param data    the data
     */
    public ListWheelAdapter(Context context, List<T> data) {
        super(context);
        this.data = data;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < data.size()) {
            T item = data.get(index);
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return getTextFormEntity(item);
        }
        return null;
    }

    /**
     * 添加一个方法，用于如果传入的集合保存的是对象实体，从实体中获取显示的文字的方法,根据需要复写
     *
     * @param item 实体
     * @return 返回的文字用于显示在滚轮tiem上，默认返回toString
     */
    public CharSequence getTextFormEntity(T item) {
        return item.toString();
    }

    @Override
    public int getItemsCount() {
        return data.size();
    }
}
