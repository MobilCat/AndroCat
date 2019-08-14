package mustafaozhan.github.com.androcat.dagger.module

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides

/**
 * Created by Mustafa Ozhan on 2018-07-22.
 */
@Module
class ViewModelModule(private val viewModel: ViewModel) {

    @Provides
    internal fun providesViewModel(): ViewModel {
        return viewModel
    }
}
