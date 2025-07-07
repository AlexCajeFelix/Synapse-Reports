package br.com.sinapse.reports.sinapsereports.application.shared.UseCaseAbstract;

public abstract class InputOutputUseCase<I, O> {
    public abstract O execute(I input);
}
