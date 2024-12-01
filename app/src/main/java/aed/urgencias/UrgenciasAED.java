package aed.urgencias;

import es.upm.aedlib.Entry;
import es.upm.aedlib.EntryImpl;
import es.upm.aedlib.Pair;
import es.upm.aedlib.map.*;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.priorityqueue.HeapPriorityQueue;
import es.upm.aedlib.priorityqueue.PriorityQueue;
import es.upm.aedlib.priorityqueue.SortedListPriorityQueue;

import java.util.Iterator;

public class UrgenciasAED implements Urgencias {

    PriorityQueue<Integer,Paciente> pacientesEsperando = new HeapPriorityQueue<>();
    Map<String, Paciente> pacientesPorDni = new HashTableMap<>();
    Integer tiempoDeEsperaAtendidos = 0;
    Integer numeroPacientesAtendidos = 0;

    @Override
    public Paciente admitirPaciente(String DNI, int prioridad, int hora) throws PacienteExisteException {
        if (pacientesPorDni.containsKey(DNI)) throw new PacienteExisteException();
        else {
            Paciente nuevoPaciente = new Paciente(DNI,prioridad,hora, hora);
            pacientesPorDni.put(DNI, nuevoPaciente);
            pacientesEsperando.enqueue(prioridad, nuevoPaciente);
            return nuevoPaciente;
        }
    }

    @Override
    public Paciente salirPaciente(String DNI, int hora) throws PacienteNoExisteException {
        if (!pacientesPorDni.containsKey(DNI)) throw new PacienteNoExisteException();
        else {
            Entry<Integer, Paciente> primerPaciente = pacientesEsperando.first();
            pacientesEsperando.remove(pacientesEsperando.first());
            Paciente paciente = pacientesPorDni.remove(DNI);
            pacientesEsperando.enqueue(primerPaciente.getKey(),primerPaciente.getValue());
            return paciente;
        }
    }

    @Override
    public Paciente cambiarPrioridad(String DNI, int nuevaPrioridad, int hora) throws PacienteNoExisteException {
        if (!pacientesPorDni.containsKey(DNI)) throw new PacienteNoExisteException();
        Paciente paciente = pacientesPorDni.get(DNI);
        if (nuevaPrioridad == paciente.getPrioridad()){
            return paciente;
        }
        else {
            Entry<Integer, Paciente> primerPaciente = pacientesEsperando.first();
            pacientesEsperando.remove(pacientesEsperando.first());
            paciente.setPrioridad(nuevaPrioridad);
            paciente.setTiempoAdmisionEnPrioridad(hora);
            pacientesEsperando.replaceKey(pacientesEsperando.enqueue(primerPaciente.getKey(),primerPaciente.getValue()), nuevaPrioridad);
            return paciente;
        }
    }

    @Override
    public Paciente atenderPaciente(int hora) {
        if (pacientesEsperando.isEmpty()) return null;
        else {
            Paciente paciente = pacientesEsperando.dequeue().getValue();
            pacientesPorDni.remove(paciente.getDNI());
            numeroPacientesAtendidos++;
            tiempoDeEsperaAtendidos += (hora - paciente.getTiempoAdmision());
            return paciente;
        }
    }

    @Override
    public void aumentaPrioridad(int maxTiempoEspera, int hora) {
        for(Entry<Integer, Paciente> entry : pacientesEsperando) {
            Paciente paciente = entry.getValue();
            int tiempoDeEsperaDePaciente = hora - paciente.getTiempoAdmisionEnPrioridad();

            if(tiempoDeEsperaDePaciente > maxTiempoEspera && paciente.getPrioridad() != 1) {
                Entry<Integer, Paciente> primerPaciente = pacientesEsperando.first();
                pacientesEsperando.remove(pacientesEsperando.first());
                paciente.setPrioridad(paciente.getPrioridad() - 1);
                paciente.setTiempoAdmisionEnPrioridad(hora);
                pacientesEsperando.replaceKey(pacientesEsperando.enqueue(primerPaciente.getKey(),primerPaciente.getValue()), paciente.getPrioridad() - 1);
            }
        }
    }


    @Override
    public Iterable<Paciente> pacientesEsperando() {
        PositionList<Paciente> iterableRespuesta = new NodePositionList<>();
        for(Entry<Integer, Paciente> entrada : pacientesEsperando) {
            iterableRespuesta.addLast(entrada.getValue());
        }
        return iterableRespuesta;
    }


    @Override
    public Paciente getPaciente(String DNI) {
        if(pacientesPorDni.containsKey(DNI)) {
            return pacientesPorDni.get(DNI);
        } else {
            return null;
        }
    }

    @Override
    public Pair<Integer, Integer> informacionEspera() {
        if(numeroPacientesAtendidos == null) return null;
        return new Pair<>(tiempoDeEsperaAtendidos, numeroPacientesAtendidos);
    }
}
