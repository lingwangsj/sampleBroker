/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SampleBrokerTestModule } from '../../../test.module';
import { APMySuffixComponent } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix.component';
import { APMySuffixService } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix.service';
import { APMySuffix } from '../../../../../../main/webapp/app/entities/ap-my-suffix/ap-my-suffix.model';

describe('Component Tests', () => {

    describe('APMySuffix Management Component', () => {
        let comp: APMySuffixComponent;
        let fixture: ComponentFixture<APMySuffixComponent>;
        let service: APMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [APMySuffixComponent],
                providers: [
                    APMySuffixService
                ]
            })
            .overrideTemplate(APMySuffixComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(APMySuffixComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(APMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new APMySuffix(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.aPS[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
