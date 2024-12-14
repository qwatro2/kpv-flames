package backend.academy.fractals.paramsgetters;

import backend.academy.fractals.params.Params;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCliParamsGetter implements ParamsGetter {
    private final Map<String, ModifierAndNumberOfArguments> map;
    protected final String[] args;

    protected AbstractCliParamsGetter(String[] args, boolean addHelp) {
        this.map = new HashMap<>();
        this.args = args;
        if (addHelp) {
            this.addParam("-h", 0, this::processHelp)
                .addParam("--help", 0, this::processHelp);
        }
    }

    protected AbstractCliParamsGetter addParam(
        String paramName, int numberOfArguments,
        ParamsModifier biConsumer
    ) {
        ModifierAndNumberOfArguments value = new ModifierAndNumberOfArguments(biConsumer, numberOfArguments);
        map.put(paramName, value);
        return this;
    }

    @Override
    public Params get() {
        Params result = new Params();

        int i = 0;
        while (i < args.length) {
            String paramName = args[i];
            ModifierAndNumberOfArguments modifierAndNumberOfArguments = getOrDefault(paramName);
            modifierAndNumberOfArguments.modifier().accept(result, i);
            if (!result.isSuccess()) {
                return result;
            }
            i += modifierAndNumberOfArguments.numberOfArguments();
            ++i;
        }

        checkProcessedParams(result);
        return result;
    }

    protected abstract void processHelp(Params params, int index);

    protected abstract void processUnknownArgument(Params params, int index);

    protected abstract void checkProcessedParams(Params params);

    private record ModifierAndNumberOfArguments(ParamsModifier modifier, int numberOfArguments) {
    }

    private ModifierAndNumberOfArguments getOrDefault(String key) {
        return map.getOrDefault(key, new ModifierAndNumberOfArguments(this::processUnknownArgument, 1));
    }
}
