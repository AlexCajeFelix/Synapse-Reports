package br.com.sinapse.reports.sinapsereports.Application.UseCase.UseCaseAbstract;

public abstract class InputOutputUseCase<I, O> {
    public abstract O execute(I input);
}
