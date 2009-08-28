/**
 * Copyright (C) 2008 nicola 
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

package org.regola.ws;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProduceMime;

@Path("/nome-servizio/")
@ProduceMime("text/xml")
public class RestManager {

	@POST
	@Path("/foo/{id}/{nome}")
	public Dto postFoo(@PathParam("id") String id, @PathParam("nome") String nome, Dto parametro) {

		return new Dto(id + ":" + nome + ":" + parametro.prova);
	}

	@PUT
	@Path("/foo/{id}/{nome}")
	public Dto putFoo(@PathParam("id") String id, @PathParam("nome") String nome, Dto parametro) {

		return new Dto(id + ":" + nome + ":" + parametro.prova);
	}

	@GET
	@Path("/foo/{id}/{nome}")
	public String getFoo(@PathParam("id") int id, @PathParam("nome") String nome) {

		return "GET di:" + id + ":" + nome;
	}

	@DELETE
	@Path("/foo/{id}/{nome}")
	public String deleteFoo(@PathParam("id") int id, @PathParam("nome") String nome) {

		return "DELETE di:" + id + ":" + nome;
	}

	
}
