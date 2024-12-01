package aed.urgencias;

import es.upm.aedlib.Pair;

public class UrgenciasAED implements Urgencias {

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

    }

    @Override
    public Iterable<Paciente> pacientesEsperando() {
        return null;
    }

    @Override
    public Paciente getPaciente(String DNI) {
        return null;
    }

    @Override
    public Pair<Integer, Integer> informacionEspera() {
        return null;
    }
}
