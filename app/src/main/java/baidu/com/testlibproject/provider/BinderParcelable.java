package baidu.com.testlibproject.provider;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

class BinderParcelable implements Parcelable {

    public IBinder iBinder;

    public static final Creator<BinderParcelable> CREATOR = new Creator<BinderParcelable>() {
        @Override
        public BinderParcelable createFromParcel(Parcel parcel) {
            return new BinderParcelable(parcel);
        }

        @Override
        public BinderParcelable[] newArray(int i) {
            return new BinderParcelable[0];
        }
    };

    public BinderParcelable(@NonNull IBinder iBinder) {
        this.iBinder = iBinder;
    }

    public BinderParcelable(Parcel parcel) {
        iBinder = parcel.readStrongBinder();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(iBinder);
    }
}
