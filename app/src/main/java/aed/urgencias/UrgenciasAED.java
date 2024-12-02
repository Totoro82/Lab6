package aed.urgencias;

import es.upm.aedlib.Entry;
import es.upm.aedlib.Pair;
import es.upm.aedlib.map.HashTableMap;
import es.upm.aedlib.map.Map;
import es.upm.aedlib.positionlist.NodePositionList;
import es.upm.aedlib.positionlist.PositionList;
import es.upm.aedlib.priorityqueue.HeapPriorityQueue;
import es.upm.aedlib.priorityqueue.PriorityQueue;
import es.upm.aedlib.priorityqueue.SortedListPriorityQueue;

public class UrgenciasAED implements Urgencias {

    PriorityQueue<String,Paciente> pacientesEsperando = new HeapPriorityQueue<>();
    Map<String, Paciente> pacientesPorDni = new HashTableMap<>();
    Integer tiempoDeEsperaAtendidos;
    Integer numeroPacientesAtendidos;

    @Override
    public Paciente admitirPaciente(String DNI, int prioridad, int hora) throws PacienteExisteException {
        return null;
    }

    @Override
    public Paciente salirPaciente(String DNI, int hora) throws PacienteNoExisteException {
        return null;
    }

    @Override
    public Paciente cambiarPrioridad(String DNI, int nuevaPrioridad, int hora) throws PacienteNoExisteException {
        return null;
    }

    @Override
    public Paciente atenderPaciente(int hora) {
        return null;
    }

    @Override
    public void aumentaPrioridad(int maxTiempoEspera, int hora) {
        for(Entry<String, Paciente> entry : pacientesEsperando) {
            Paciente paciente = entry.getValue();
            int tiempoDeEsperaDePaciente = paciente.getTiempoAdmision();
            if(tiempoDeEsperaDePaciente > maxTiempoEspera && paciente.getPrioridad() != 1) {
                paciente.setPrioridad(paciente.getPrioridad() - 1);
                paciente.setTiempoAdmisionEnPrioridad(hora);
            }
        }
    }


    @Override
    public Iterable<Paciente> pacientesEsperando() {
        PositionList<Paciente> iterableRespuesta = new NodePositionList<>();
        for(Entry<String, Paciente> entrada : pacientesEsperando) {
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
