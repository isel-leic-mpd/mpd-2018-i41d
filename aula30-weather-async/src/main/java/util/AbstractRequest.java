/*
 * Copyright (c) 2017, Miguel Gamboa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

/**
 * @author Miguel Gamboa
 *         created on 08-03-2017
 */
public abstract class AbstractRequest implements IRequest{
    /**
     * Template Method.
     * Delegates on Hook Method the changeable part of the algorithm.
     * This one is final and cannot be modified.
     */
    @Override
    public final Stream<String> getContent(String uri) {
        InputStream data = openStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(data));
        return reader.lines();
    }

    /**
     * Hook Method.
     */
    abstract InputStream openStream(String path);
}
