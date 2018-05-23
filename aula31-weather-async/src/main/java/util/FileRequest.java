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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author Miguel Gamboa
 *         created on 08-03-2017
 */
public class FileRequest implements IRequest{

    /**
     * !!!! ALERTA ainda é BLOQUEANTE
     */
    @Override
    public CompletableFuture<String> getContent(String uri) {
        String[] parts = uri.split("/");
        String path = parts[parts.length-1]
                .replace('?', '-')
                .replace('&', '-')
                .replace('=', '-')
                .replace(',', '-')
                .substring(0,68);
        try{
            InputStream in = ClassLoader.getSystemResource(path).openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String content = reader.lines().collect(joining("\n")); // BLOQUEADO
            CompletableFuture<String> promise = new CompletableFuture<>();
            promise.complete(content);
            return promise;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
